package com.hendraanggrian.packr.scene

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.desc
import com.badlogicgames.packr.toPlatform
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hendraanggrian.packr.MinimizeOption
import com.hendraanggrian.packr.PackrItem
import com.hendraanggrian.packr.R
import com.hendraanggrian.packr.notEmptyOrNull
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE
import javafx.scene.control.ButtonType.CANCEL
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority.ALWAYS
import kotfx.bindings.booleanBindingOf
import kotfx.bindings.or
import kotfx.collections.toObservableList
import kotfx.dialogs.addButton
import kotfx.dialogs.buttons
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.infoAlert
import kotfx.gap
import kotfx.scene.button
import kotfx.scene.buttonBar
import kotfx.scene.choiceBox
import kotfx.scene.gridPane
import kotfx.scene.label
import kotfx.scene.textField
import kotfx.scene.vbox
import kotfx.setPadding
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import org.apache.commons.io.IOUtils
import java.awt.Desktop.getDesktop
import java.io.File

class PackrTab(initialFile: File) : Tab(initialFile.nameWithoutExtension) {

    companion object {
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    private lateinit var item: PackrItem

    private lateinit var processButton: Button

    private lateinit var platformChoice: ChoiceBox<PackrConfig.Platform>
    private lateinit var jdkField: TextField
    private lateinit var executableField: TextField
    private lateinit var classpathList: ListView<String>
    private lateinit var mainclassField: TextField
    private lateinit var resourcesList: ListView<String>
    private lateinit var vmargsList: ListView<String>
    private lateinit var minimizeChoice: ChoiceBox<MinimizeOption>
    private lateinit var outputField: TextField
    private lateinit var iconField: TextField
    private lateinit var bundleField: TextField

    init {
        initialFile.inputStream().use { item = GSON.fromJson(IOUtils.toString(it), PackrItem::class.java) }
        content = vbox {
            gridPane {
                padding = Insets(8.0)
                gap = 8.0

                label("Platform") row 0 col 0
                platformChoice = choiceBox(PackrConfig.Platform.values().toObservableList()) {
                    item.platform?.let { value = it.toPlatform() }
                } row 0 col 1 colSpan 3

                label("JDK") row 1 col 0
                jdkField = textField {
                    promptText = "Directory or zip initialPath containing a JRE"
                    item.jdk?.let { text = it }
                } row 1 col 1 hpriority ALWAYS
                button(graphic = ImageView(R.image.btn_home)) {
                    tooltip = Tooltip("Use java.home")
                    setOnAction { System.getProperty("java.home")?.let { jdkField.text = File(it).parent } }
                } row 1 col 2
                button(graphic = ImageView(R.image.btn_folder)) {
                    tooltip = Tooltip("Browse")
                    setOnAction { directoryChooser().showDialog(scene.window)?.let { jdkField.text = it.path } }
                } row 1 col 3

                label("Executable") row 2 col 0
                executableField = textField {
                    promptText = "Name of the native executable"
                    item.executable?.let { text = it }
                } row 2 col 1 colSpan 3

                label("Classpath") row 3 col 0
                classpathList = textListView(initialFile, "Jar initialPath", true, false, "jar") {
                    item.classpath?.let { items.addAll(it) }
                } row 3 col 1 colSpan 3 vpriority ALWAYS

                label("Main class") row 4 col 0
                mainclassField = textField {
                    promptText = "Fully qualified name of the main class"
                    item.mainclass?.let { text = it }
                } row 4 col 1 colSpan 3

                label("Resources") row 5 col 0
                resourcesList = textListView(initialFile, "Resource files", true, true) {
                    minHeight = 77.0
                    maxHeight = 77.0
                    item.resources?.let { items.addAll(it) }
                } row 5 col 1 colSpan 3

                label("VM args") row 6 col 0
                vmargsList = textListView(initialFile, "Arguments") {
                    minHeight = 77.0
                    maxHeight = 77.0
                    item.vmargs?.let { items.addAll(it) }
                } row 6 col 1 colSpan 3

                label("Minimize JRE") row 7 col 0
                minimizeChoice = choiceBox(MinimizeOption.values().toObservableList()) {
                    item.minimizejre?.let { value = MinimizeOption.byDesc(it) }
                } row 7 col 1 colSpan 3

                label("Output directory") row 8 col 0
                outputField = textField {
                    promptText = "Output directory"
                    item.output?.let { text = it }
                } row 8 col 1 colSpan 2
                button(graphic = ImageView(R.image.btn_folder)) {
                    tooltip = Tooltip("Browse")
                } row 8 col 3

                label("Mac icon") row 9 col 0
                iconField = textField {
                    promptText = "Optional icns file"
                    item.icon?.let { text = it }
                } row 9 col 1 colSpan 2
                button(graphic = ImageView(R.image.btn_folder)) {
                    tooltip = Tooltip("Browse")
                } row 9 col 3

                label("Mac bundle") row 10 col 0
                bundleField = textField {
                    promptText = "Optional bundle identifier"
                    item.bundle?.let { text = it }
                } row 10 col 1 colSpan 3
            }
            buttonBar {
                setPadding(left = 8.0, right = 8.0, bottom = 8.0)
                button("Save", ImageView(R.image.btn_save)) {
                    setOnAction {
                        initialFile.writeText(GSON.toJson(item.apply {
                            platform = platformChoice.value?.desc
                            jdk = jdkField.text.notEmptyOrNull
                            executable = executableField.text.notEmptyOrNull
                            classpath = ArrayList(classpathList.items)
                            mainclass = mainclassField.text.notEmptyOrNull
                            vmargs = ArrayList(vmargsList.items)
                            minimizejre = minimizeChoice.value.desc.notEmptyOrNull
                            output = outputField.text.notEmptyOrNull
                            icon = iconField.text.notEmptyOrNull
                            bundle = bundleField.text.notEmptyOrNull
                        }))
                        infoAlert("${initialFile.nameWithoutExtension} successfully saved!").showAndWait()
                    }
                }
                processButton = button("Process", ImageView(R.image.btn_process)) {
                    setOnAction {
                        val dialog = infoAlert("Please wait...") {
                            buttons.clear()
                            show()
                        }
                        launch {
                            val outputFile = File(initialFile.parent, outputField.text)
                            try {
                                Packr().pack(PackrConfig(
                                    platformChoice.value,
                                    jdkField.text,
                                    executableField.text,
                                    classpathList.items.map { File(initialFile.parent, it).path },
                                    mainclassField.text,
                                    outputFile
                                ).apply {
                                    resources = resourcesList.items.map { File(initialFile.parent, it) }
                                    vmArgs = vmargsList.items
                                    minimizeJre = minimizeChoice.value.desc
                                    iconField.text?.let { iconResource = File(initialFile.parent, it) }
                                    bundleField.text?.let { bundleIdentifier = it }
                                })
                                launch(JavaFx) {
                                    dialog.addButton(CANCEL)
                                    dialog.close()
                                    infoAlert("Packr process finished.") { addButton("Open folder", CANCEL_CLOSE) }
                                        .showAndWait()
                                        .filter { it.buttonData == CANCEL_CLOSE }
                                        .ifPresent { getDesktop().open(outputFile.parentFile) }
                                }
                            } catch (e: Exception) {
                                launch(JavaFx) {
                                    dialog.addButton(CANCEL)
                                    dialog.close()
                                    errorAlert(e.message ?: "Unknown error!").showAndWait()
                                }
                            }
                        }
                    }
                }
            }
        }
        processButton.disableProperty().bind(platformChoice.valueProperty().isNull
            or booleanBindingOf(jdkField.textProperty()) { !File(jdkField.text).exists() }
            or executableField.textProperty().isEmpty
            or mainclassField.textProperty().isEmpty
            or minimizeChoice.valueProperty().isNull
            or outputField.textProperty().isEmpty)
    }
}