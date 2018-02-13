package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.scene.textListView
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority.ALWAYS
import javafx.stage.Stage
import kotfx.bindings.booleanBindingOf
import kotfx.bindings.or
import kotfx.collections.toObservableList
import kotfx.dialogs.directoryChooser
import kotfx.gap
import kotfx.scene.button
import kotfx.scene.choiceBox
import kotfx.scene.gridPane
import kotfx.scene.label
import kotfx.scene.menu
import kotfx.scene.menuBar
import kotfx.scene.menuItem
import kotfx.scene.textField
import kotfx.scene.toolBar
import kotfx.scene.vbox
import kotfx.setMinSize
import kotfx.setSize
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.lang.System.getProperty

class App : Application() {

    companion object {
        @JvmStatic fun main(args: Array<String>) = Application.launch(App::class.java, *args)
    }

    private lateinit var processButton: Button

    private lateinit var platformChoice: ChoiceBox<Platform>
    private lateinit var jdkField: TextField
    private lateinit var executableField: TextField
    private lateinit var classpathList: ListView<String>
    private lateinit var mainClassField: TextField
    private lateinit var resourcesList: ListView<String>
    private lateinit var vmArgsList: ListView<String>
    private lateinit var minimizeChoice: ChoiceBox<MinimizeOption>
    private lateinit var outputField: TextField
    private lateinit var iconField: TextField
    private lateinit var bundleField: TextField

    override fun start(stage: Stage) = stage.apply {
        title = "Packr"
        scene = Scene(vbox {
            menuBar {
                isUseSystemMenuBar = true
                menu("File") {
                    menuItem("Save") { }
                }
                menu("Edit") {
                    menuItem("Clear") {
                        setOnAction {
                            platformChoice.selectionModel.clearSelection()
                            jdkField.clear()
                            executableField.clear()
                            classpathList.items.clear()
                            mainClassField.clear()
                            resourcesList.items.clear()
                            vmArgsList.items.clear()
                            minimizeChoice.selectionModel.clearSelection()
                            outputField.clear()
                            iconField.clear()
                            bundleField.clear()
                        }
                    }
                }
            }
            toolBar {
                button("Save") {
                }
                processButton = button("Process") {
                    setOnAction {
                        val config = PackrConfig(
                            platformChoice.value,
                            jdkField.text,
                            executableField.text,
                            classpathList.items,
                            mainClassField.text,
                            File(outputField.text)
                        ).apply {
                            vmArgs = vmArgsList.items
                            minimizeJre = minimizeChoice.value.value
                            iconResource = File(iconField.text)
                            bundleIdentifier = bundleField.text
                        }
                        
                        launch {
                            Packr().pack(config)
                        }
                    }
                }
            }
            gridPane {
                padding = Insets(8.0)
                gap = 8.0

                label("Platform") row 0 col 0
                platformChoice = choiceBox(Platform.values().toObservableList()) {
                } row 0 col 1 colSpan 3

                label("JDK") row 1 col 0
                jdkField = textField() row 1 col 1 hpriority ALWAYS
                button(graphic = ImageView(R.image.ic_home)) {
                    tooltip = Tooltip("Use java.home")
                    setOnAction { getProperty("java.home")?.let { jdkField.text = it } }
                } row 1 col 2
                button(graphic = ImageView(R.image.ic_folder)) {
                    tooltip = Tooltip("Browse")
                    setOnAction { directoryChooser().showDialog(stage)?.let { jdkField.text = it.path } }
                } row 1 col 3

                label("Executable") row 2 col 0
                executableField = textField {
                } row 2 col 1 colSpan 3

                label("Classpath") row 3 col 0
                classpathList = textListView("Jar file", true, false, "jar") row 3 col 1 colSpan 3 vpriority ALWAYS

                label("Main class") row 4 col 0
                mainClassField = textField() row 4 col 1 colSpan 3

                label("Resources") row 5 col 0
                resourcesList = textListView("Resource files", true, true) {
                    minHeight = 77.0
                    maxHeight = 77.0
                } row 5 col 1 colSpan 3

                label("VM args") row 6 col 0
                vmArgsList = textListView("Arguments") {
                    minHeight = 77.0
                    maxHeight = 77.0
                } row 6 col 1 colSpan 3

                label("Minimize JRE") row 7 col 0
                minimizeChoice = choiceBox(MinimizeOption.values().toObservableList()) { } row 7 col 1 colSpan 3

                label("Output directory") row 8 col 0
                outputField = textField { } row 8 col 1 colSpan 2
                button(graphic = ImageView(R.image.ic_folder)) {
                    tooltip = Tooltip("Browse")
                } row 8 col 3

                label("Mac icon") row 9 col 0
                iconField = textField { } row 9 col 1 colSpan 2
                button(graphic = ImageView(R.image.ic_folder)) {
                    tooltip = Tooltip("Browse")
                } row 9 col 3

                label("Mac bundle") row 10 col 0
                bundleField = textField { } row 10 col 1 colSpan 3
            }
        })
        setMinSize(500.0, 700.0)
        setSize(500.0, 700.0)

        processButton.disableProperty().bind(platformChoice.valueProperty().isNull
            or booleanBindingOf(jdkField.textProperty()) { !File(jdkField.text).exists() }
            or executableField.textProperty().isEmpty
            or mainClassField.textProperty().isEmpty
            or minimizeChoice.valueProperty().isNull
            or booleanBindingOf(outputField.textProperty()) { !File(outputField.text).exists() })
    }.show()
}