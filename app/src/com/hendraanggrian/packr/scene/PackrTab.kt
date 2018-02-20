package com.hendraanggrian.packr.scene

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.badlogicgames.packr.desc
import com.badlogicgames.packr.toPlatform
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hendraanggrian.packr.PackrItem
import com.hendraanggrian.packr.R
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
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
import kotfx.coroutines.launchFX
import kotfx.coroutines.onAction
import kotfx.dialogs.addButton
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.fileChooser
import kotfx.dialogs.infoAlert
import kotfx.gap
import kotfx.layout.anchorPane
import kotfx.layout.borderPane
import kotfx.layout.button
import kotfx.layout.buttonBar
import kotfx.layout.choiceBox
import kotfx.layout.gridPane
import kotfx.layout.label
import kotfx.layout.textField
import kotfx.layout.tooltip
import kotfx.layout.vbox
import kotfx.maxSize
import kotfx.setPadding
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.apache.commons.io.IOUtils
import java.awt.Desktop.getDesktop
import java.io.File
import java.lang.System.getProperty

/** A tab of Packr configuration. */
class PackrTab(jsonFile: File) : Tab(jsonFile.nameWithoutExtension) {

    companion object {
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    private lateinit var item: PackrItem
    private lateinit var platformChoice: ChoiceBox<Platform>
    private lateinit var jdkField: TextField
    private lateinit var executableField: TextField
    private lateinit var classpathList: ListView<String>
    private lateinit var mainclassField: TextField
    private lateinit var vmargsList: ListView<String>
    private lateinit var resourcesList: ListView<String>
    private lateinit var minimizeChoice: ChoiceBox<Minimize>
    private lateinit var outputField: TextField
    private lateinit var iconField: TextField
    private lateinit var bundleField: TextField
    private lateinit var loadingPane: Pane

    init {
        launch {
            jsonFile.inputStream().use {
                item = GSON.fromJson(IOUtils.toString(it), PackrItem::class.java)
                launchFX {
                    item.platform?.let { platformChoice.value = it.toPlatform() }
                    item.jdk?.let { jdkField.text = it }
                    item.executable?.let { executableField.text = it }
                    item.classpath?.let { classpathList.items.addAll(it) }
                    item.mainclass?.let { mainclassField.text = it }
                    item.vmargs?.let { vmargsList.items.addAll(it) }
                    item.resources?.let { resourcesList.items.addAll(it) }
                    item.minimizejre?.let { minimizeChoice.value = Minimize.byDesc(it) }
                    item.output?.let { outputField.text = it }
                    item.icon?.let { iconField.text = it }
                    item.bundle?.let { bundleField.text = it }
                    contents -= loadingPane
                }
            }
        }
        content = anchorPane {
            vbox {
                gridPane {
                    padding = Insets(8.0)
                    gap = 8.0

                    label("Platform") row 0 col 0
                    platformChoice = choiceBox(Platform.values().toObservableList()) {
                        tooltip("One of \"windows32\", \"windows64\", \"linux32\", \"linux64\", \"mac\".")
                    } row 0 col 1 colSpan 3

                    label("JDK") row 1 col 0
                    jdkField = textField {
                        tooltip("Directory or ZIP file of an OpenJDK or Oracle JDK build containing JRE.")
                        promptText = "JDK"
                    } row 1 col 1 hpriority ALWAYS
                    button(graphic = ImageView(R.image.btn_home)) {
                        tooltip("Use java.home")
                        onAction { getProperty("java.home")?.let { jdkField.text = File(it).parent } }
                    } row 1 col 2
                    button(graphic = ImageView(R.image.btn_folder)) {
                        tooltip("Browse")
                        onAction { directoryChooser(jsonFile.parentFile).showDialog(scene.window)?.let { jdkField.text = it.path } }
                    } row 1 col 3

                    label("Executable") row 2 col 0
                    executableField = textField {
                        tooltip("Name of the native executable, without extension such as \".exe\".")
                        promptText = "Executable"
                    } row 2 col 1 colSpan 3

                    label("Classpath") row 3 col 0
                    classpathList = textListView(jsonFile, "Jar file", true, false, "jar") {
                        tooltip("File locations of the JAR files to package.")
                    } row 3 col 1 colSpan 3 vpriority ALWAYS

                    label("Main class") row 4 col 0
                    mainclassField = textField {
                        tooltip("The fully qualified name of the main class, using dots to delimit package names.")
                        promptText = "Main class"
                    } row 4 col 1 colSpan 3

                    label("VM args") row 5 col 0
                    vmargsList = textListView(jsonFile, "Arguments") {
                        tooltip("List of arguments for the JVM, without leading dashes, e.g. \"Xmx1G\".")
                        minHeight = 77.0
                        maxHeight = 77.0
                    } row 5 col 1 colSpan 3

                    label("Resources") row 6 col 0
                    resourcesList = textListView(jsonFile, "Resource files", true, true) {
                        tooltip("List of files and directories to be packaged next to the native executable.")
                        minHeight = 77.0
                        maxHeight = 77.0
                    } row 6 col 1 colSpan 3

                    label("Minimize JRE") row 7 col 0
                    minimizeChoice = choiceBox(Minimize.values().toObservableList()) {
                        tooltip("Minimize the JRE by removing directories and files as specified by an additional config file.")
                    } row 7 col 1 colSpan 3

                    label("Output directory") row 8 col 0
                    outputField = textField {
                        tooltip("The output directory.")
                        promptText = "Output directory"
                    } row 8 col 1 colSpan 2
                    button(graphic = ImageView(R.image.btn_folder)) {
                        tooltip("Browse")
                        onAction {
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
                    } row 9 col 1 colSpan 2
                    button(graphic = ImageView(R.image.btn_folder)) {
                        tooltip("Browse")
                        onAction {
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
                    } row 10 col 1 colSpan 3
                }
                buttonBar {
                    setPadding(left = 8.0, right = 8.0, bottom = 8.0)
                    button("Save", ImageView(R.image.btn_save)) {
                        onAction(CommonPool) {
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
                            launchFX { infoAlert("${jsonFile.nameWithoutExtension} successfully saved!").showAndWait() }
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
                        onAction {
                            isClosable = false
                            contents += loadingPane
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
                                    launchFX {
                                        isClosable = true
                                        contents -= loadingPane
                                        infoAlert("Packr process finished.") { addButton("Open folder", CANCEL_CLOSE) }
                                            .showAndWait()
                                            .filter { it.buttonData == CANCEL_CLOSE }
                                            .ifPresent { getDesktop().open(outputFile.parentFile) }
                                    }
                                } catch (e: Exception) {
                                    launchFX {
                                        isClosable = true
                                        contents -= loadingPane
                                        errorAlert(e.message ?: "Unknown error!").showAndWait()
                                    }
                                }
                            }
                        }
                    }
                }
            } anchor 0
            loadingPane = borderPane {
                background = Background(BackgroundFill(rgb(255, 255, 255, 0.75), CornerRadii.EMPTY, Insets.EMPTY))
                center = kotfx.layout.progressIndicator { maxSize = 156.0 }
            } anchor 0
        }
    }

    /** Tab content is always a [Pane]. */
    private inline val contents: ObservableList<Node> get() = (content as Pane).children

    /** Returns not empty text or null instead. */
    private inline val String.notEmptyOrNull: String? get() = if (isEmpty()) null else this

    /** Enumeration representing Packr JRE minimization using [Platform] convention. */
    private enum class Minimize(val desc: String) {
        Soft("soft"),
        Hard("hard"),
        OracleJRE8("oraclejre8");

        companion object {
            fun byDesc(desc: String): Minimize = values().single { it.desc.equals(desc, true) }
        }
    }
}