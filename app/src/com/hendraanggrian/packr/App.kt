package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig.Platform
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Accordion
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode.MULTIPLE
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.scene.control.cell.TextFieldListCell.forListView
import javafx.scene.image.ImageView
import javafx.stage.Stage
import kotfx.bindings.isEmpty
import kotfx.collections.toObservableList
import kotfx.dialogs.fileChooser
import kotfx.gap
import kotfx.scene.accordion
import kotfx.scene.button
import kotfx.scene.choiceBox
import kotfx.scene.contextMenu
import kotfx.scene.gridPane
import kotfx.scene.label
import kotfx.scene.listView
import kotfx.scene.menuBar
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem
import kotfx.scene.textField
import kotfx.scene.toolBar
import kotfx.scene.vbox
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch

class App : Application() {

    companion object {
        @JvmStatic fun main(args: Array<String>) = Application.launch(App::class.java, *args)
    }

    private lateinit var platformChoice: ChoiceBox<Platform>
    private lateinit var jdkField: TextField
    private lateinit var executableField: TextField
    private lateinit var classpathAccordion: Accordion
    private lateinit var mainClassField: TextField
    private lateinit var resourcesList: ListView<String>
    private lateinit var vmArgsList: ListView<String>
    private lateinit var minimizeChoice: ChoiceBox<MinimizeOption>
    private lateinit var outputField: TextField
    private lateinit var iconField: TextField
    private lateinit var bundleField: TextField

    override fun start(stage: Stage) {
        stage.scene = Scene(vbox {
            menuBar {
            }
            toolBar {
                button {
                }
            }
            gridPane {
                padding = Insets(8.0)
                gap = 8.0

                label("Platform") row 0 col 0
                platformChoice = choiceBox(Platform.values().toObservableList()) {

                } row 0 col 1 colSpan 3

                label("JDK") row 1 col 0
                jdkField = textField() row 1 col 1
                button(graphic = ImageView(R.image.ic_home)) {
                    tooltip = Tooltip("Use java.home")
                } row 1 col 2
                button(graphic = ImageView(R.image.ic_folder)) {
                    tooltip = Tooltip("Browse")
                } row 1 col 3

                label("Executable") row 2 col 0
                executableField = textField {
                } row 2 col 1 colSpan 3

                label("Classpath") row 3 col 0
                classpathAccordion = accordion(stage.classpathPane("Include"), stage.classpathPane("Exclude")) {
                    minHeight = 192.0
                    maxHeight = 192.0
                    expandedPane = panes[0]
                    expandedPaneProperty().addListener { _, _, _ ->
                        launch(JavaFx) {
                            delay(1000)
                            if (expandedPane == null) expandedPane = panes[0]
                        }
                    }
                } row 3 col 1 colSpan 3

                label("Main class") row 4 col 0
                mainClassField = textField() row 4 col 1 colSpan 3

                label("Resources") row 5 col 0
                resourcesList = listView<String> {
                    maxHeight = 77.0
                } row 5 col 1 colSpan 3

                label("VM args") row 6 col 0
                vmArgsList = listView<String> {
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
        stage.show()
    }

    private fun Stage.classpathPane(title: String) = kotfx.scene.titledPane(title, listView<String> {
        selectionModel.selectionMode = MULTIPLE
        isEditable = true
        cellFactory = forListView()
        setOnEditCommit {
            items[it.index] = it.newValue
            items.sort()
        }
        contextMenu = contextMenu {
            menuItem("Add") {
                setOnAction {
                    this@listView.items.add(".jar")
                    this@listView.items.sort()
                }
            }
            menuItem("Browse") {
                setOnAction {
                    fileChooser("Jar file", "*.jar").showOpenMultipleDialog(this@classpathPane)?.forEach { file ->
                        this@listView.items.add(file.absolutePath)
                        this@listView.items.sort()
                    }
                }
            }
            separatorMenuItem()
            menuItem("Delete") {
                disableProperty().bind(this@listView.selectionModel.selectedItems.isEmpty)
                setOnAction { this@listView.selectionModel.selectedIndices.forEach { this@listView.items.removeAt(it) } }
            }
            menuItem("Clear") {
                disableProperty().bind(this@listView.items.isEmpty)
                setOnAction { this@listView.items.clear() }
            }
        }
    })
}