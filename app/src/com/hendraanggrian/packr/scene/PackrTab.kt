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
import com.hendraanggrian.packr.minus
import com.hendraanggrian.packr.relativeTo
import javafx.geometry.Insets
import javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane.setBottomAnchor
import javafx.scene.layout.AnchorPane.setLeftAnchor
import javafx.scene.layout.AnchorPane.setRightAnchor
import javafx.scene.layout.AnchorPane.setTopAnchor
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority.ALWAYS
import javafx.scene.paint.Color.rgb
import javafx.stage.FileChooser.ExtensionFilter
import kotfx.bindings.booleanBindingOf
import kotfx.bindings.or
import kotfx.collections.toObservableList
import kotfx.dialogs.addButton
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.fileChooser
import kotfx.dialogs.infoAlert
import kotfx.gap
import kotfx.maxSize
import kotfx.scene.anchorPane
import kotfx.scene.button
import kotfx.scene.buttonBar
import kotfx.scene.choiceBox
import kotfx.scene.gridPane
import kotfx.scene.label
import kotfx.scene.textField
import kotfx.scene.tooltip
import kotfx.scene.vbox
import kotfx.setPadding
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import org.apache.commons.io.IOUtils
import java.awt.Desktop.getDesktop
import java.io.File

class PackrTab(jsonFile: File) : Tab(jsonFile.nameWithoutExtension) {

    companion object {
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    private lateinit var item: PackrItem
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
        jsonFile.inputStream().use { item = GSON.fromJson(IOUtils.toString(it), PackrItem::class.java) }
        content = anchorPane {
            vbox {
                gridPane {
                    padding = Insets(8.0)
                    gap = 8.0

                    label("Platform") row 0 col 0
                    platformChoice = choiceBox(PackrConfig.Platform.values().toObservableList()) {
                        tooltip("One of \"windows32\", \"windows64\", \"linux32\", \"linux64\", \"mac\".")
                        item.platform?.let { value = it.toPlatform() }
                    } row 0 col 1 colSpan 3

                    label("JDK") row 1 col 0
                    jdkField = textField {
                        tooltip("Directory or ZIP file of an OpenJDK or Oracle JDK build containing JRE.")
                        promptText = "JDK"
                        item.jdk?.let { text = it }
                    } row 1 col 1 hpriority ALWAYS
                    button(graphic = ImageView(R.image.btn_home)) {
                        tooltip("Use java.home")
                        setOnAction { System.getProperty("java.home")?.let { jdkField.text = File(it).parent } }
                    } row 1 col 2
                    button(graphic = ImageView(R.image.btn_folder)) {
                        tooltip("Browse")
                        setOnAction { directoryChooser(jsonFile.parentFile).showDialog(scene.window)?.let { jdkField.text = it.path } }
                    } row 1 col 3

                    label("Executable") row 2 col 0
                    executableField = textField {
                        tooltip("Name of the native executable, without extension such as \".exe\".")
                        promptText = "Executable"
                        item.executable?.let { text = it }
                    } row 2 col 1 colSpan 3

                    label("Classpath") row 3 col 0
                    classpathList = textListView(jsonFile, "Jar file", true, false, "jar") {
                        tooltip("File locations of the JAR files to package.")
                        item.classpath?.let { items.addAll(it) }
                    } row 3 col 1 colSpan 3 vpriority ALWAYS

                    label("Main class") row 4 col 0
                    mainclassField = textField {
                        tooltip("The fully qualified name of the main class, using dots to delimit package names.")
                        promptText = "Main class"
                        item.mainclass?.let { text = it }
                    } row 4 col 1 colSpan 3

                    label("VM args") row 5 col 0
                    vmargsList = textListView(jsonFile, "Arguments") {
                        tooltip("List of arguments for the JVM, without leading dashes, e.g. \"Xmx1G\".")
                        minHeight = 77.0
                        maxHeight = 77.0
                        item.vmargs?.let { items.addAll(it) }
                    } row 5 col 1 colSpan 3

                    label("Resources") row 6 col 0
                    resourcesList = textListView(jsonFile, "Resource files", true, true) {
                        tooltip("List of files and directories to be packaged next to the native executable.")
                        minHeight = 77.0
                        maxHeight = 77.0
                        item.resources?.let { items.addAll(it) }
                    } row 6 col 1 colSpan 3

                    label("Minimize JRE") row 7 col 0
                    minimizeChoice = choiceBox(MinimizeOption.values().toObservableList()) {
                        tooltip("Minimize the JRE by removing directories and files as specified by an additional config file.")
                        item.minimizejre?.let { value = MinimizeOption.byDesc(it) }
                    } row 7 col 1 colSpan 3

                    label("Output directory") row 8 col 0
                    outputField = textField {
                        tooltip("The output directory.")
                        promptText = "Output directory"
                        item.output?.let { text = it }
                    } row 8 col 1 colSpan 2
                    button(graphic = ImageView(R.image.btn_folder)) {
                        tooltip("Browse")
                        setOnAction {
                            directoryChooser(jsonFile.parentFile)
                                .showDialog(scene.window)
                                ?.let { file ->
                                    when {
                                        file relativeTo jsonFile -> outputField.text = file - jsonFile
                                        else -> errorAlert("Folder is not relative to initial path.").showAndWait()
                                    }
                                }
                        }
                    } row 8 col 3

                    label("Mac icon") row 9 col 0
                    iconField = textField {
                        tooltip("Location of an AppBundle icon resource (.icns file).")
                        promptText = "Mac icon"
                        item.icon?.let { text = it }
                    } row 9 col 1 colSpan 2
                    button(graphic = ImageView(R.image.btn_folder)) {
                        tooltip("Browse")
                        setOnAction {
                            fileChooser(jsonFile.parentFile, null, ExtensionFilter("Mac icon resources", "*.icns"))
                                .showOpenDialog(scene.window)
                                ?.let { file ->
                                    when {
                                        file relativeTo jsonFile -> iconField.text = file - jsonFile
                                        else -> errorAlert("File is not relative to initial path.").showAndWait()
                                    }
                                }
                        }
                    } row 9 col 3

                    label("Mac bundle") row 10 col 0
                    bundleField = textField {
                        tooltip("The bundle identifier of your Java application, e.g. \"com.my.app\".")
                        promptText = "Mac bundle"
                        item.bundle?.let { text = it }
                    } row 10 col 1 colSpan 3
                }
                buttonBar {
                    setPadding(left = 8.0, right = 8.0, bottom = 8.0)
                    button("Save", ImageView(R.image.btn_save)) {
                        setOnAction {
                            jsonFile.writeText(GSON.toJson(item.apply {
                                platform = platformChoice.value?.desc
                                jdk = jdkField.text.notEmptyOrNull
                                executable = executableField.text.notEmptyOrNull
                                classpath = ArrayList(classpathList.items)
                                mainclass = mainclassField.text.notEmptyOrNull
                                vmargs = ArrayList(vmargsList.items)
                                resources = ArrayList(resourcesList.items)
                                minimizejre = minimizeChoice.value.desc.notEmptyOrNull
                                output = outputField.text.notEmptyOrNull
                                icon = iconField.text.notEmptyOrNull
                                bundle = bundleField.text.notEmptyOrNull
                            }))
                            infoAlert("${jsonFile.nameWithoutExtension} successfully saved!").showAndWait()
                        }
                    }
                    button("Process", ImageView(R.image.btn_process)) {
                        isDefaultButton = true
                        disableProperty().bind(platformChoice.valueProperty().isNull
                            or booleanBindingOf(jdkField.textProperty()) { !File(jdkField.text).exists() }
                            or executableField.textProperty().isEmpty
                            or mainclassField.textProperty().isEmpty
                            or minimizeChoice.valueProperty().isNull
                            or outputField.textProperty().isEmpty)
                        setOnAction {
                            val loadingPane = kotfx.scene.borderPane {
                                background = Background(BackgroundFill(rgb(255, 255, 255, 0.75), CornerRadii.EMPTY, Insets.EMPTY))
                                center = kotfx.scene.progressIndicator { maxSize = 156.0 }
                                setTopAnchor(this, 0.0)
                                setBottomAnchor(this, 0.0)
                                setLeftAnchor(this, 0.0)
                                setRightAnchor(this, 0.0)
                            }
                            isClosable = false
                            mainPane.children += loadingPane
                            launch {
                                val outputFile = File(jsonFile.parent, outputField.text)
                                try {
                                    Packr().pack(PackrConfig(
                                        platformChoice.value,
                                        jdkField.text,
                                        executableField.text,
                                        classpathList.items.map { File(jsonFile.parent, it).path },
                                        mainclassField.text,
                                        outputFile
                                    ).apply {
                                        vmArgs = vmargsList.items
                                        resources = resourcesList.items.map { File(jsonFile.parent, it) }
                                        minimizeJre = minimizeChoice.value.desc
                                        iconField.text?.let { iconResource = File(jsonFile.parent, it) }
                                        bundleField.text?.let { bundleIdentifier = it }
                                    })
                                    launch(JavaFx) {
                                        isClosable = true
                                        mainPane.children -= loadingPane
                                        infoAlert("Packr process finished.") { addButton("Open folder", CANCEL_CLOSE) }
                                            .showAndWait()
                                            .filter { it.buttonData == CANCEL_CLOSE }
                                            .ifPresent { getDesktop().open(outputFile.parentFile) }
                                    }
                                } catch (e: Exception) {
                                    launch(JavaFx) {
                                        isClosable = true
                                        mainPane.children -= loadingPane
                                        errorAlert(e.message ?: "Unknown error!").showAndWait()
                                    }
                                }
                            }
                        }
                    }
                }
            } anchor 0
        }
    }

    private inline val mainPane: Pane get() = content as Pane

    /** Returns not empty text or null instead. */
    private inline val String.notEmptyOrNull: String? get() = if (isEmpty()) null else this
}