package com.hendraanggrian.packr

import com.google.gson.Gson
import com.hendraanggrian.packr.scene.packrTab
import javafx.application.Application
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority.ALWAYS
import javafx.scene.layout.Region.USE_PREF_SIZE
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotfx.bindings.booleanBindingOf
import kotfx.bindings.isEmpty
import kotfx.bindings.lessEq
import kotfx.bindings.sizeBinding
import kotfx.dialogs.fileChooser
import kotfx.icon
import kotfx.maxSize
import kotfx.scene.anchorPane
import kotfx.scene.borderPane
import kotfx.scene.hyperlink
import kotfx.scene.menu
import kotfx.scene.menuBar
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem
import kotfx.scene.setFont
import kotfx.scene.tabPane
import kotfx.scene.text
import kotfx.scene.vbox
import kotfx.setMinSize
import kotfx.setSize
import java.io.File

class App : Application() {

    companion object {
        private val PACKR_FILE_FILTER = FileChooser.ExtensionFilter("Packr configuration initialPath", "*.json")

        @JvmStatic fun main(args: Array<String>) = Application.launch(App::class.java, *args)
    }

    private lateinit var tabPane: TabPane

    override fun start(stage: Stage) = stage.apply {
        icon = Image(R.image.ic_packr)
        title = "Packr"
        scene = Scene(vbox {
            menuBar {
                isUseSystemMenuBar = true
                menu("File") {
                    menuItem("New") { setOnAction { stage.create() } }
                    menuItem("Open...", ImageView(R.image.menu_folder)) { setOnAction { stage.open() } }
                }
            }
            anchorPane {
                tabPane = tabPane() anchor 0
                borderPane {
                    visibleProperty().bind(tabPane.tabs.isEmpty)
                    center = kotfx.scene.textFlow {
                        maxSize = USE_PREF_SIZE
                        text("No tabs opened,") { setFont(size = 18.0) }
                        hyperlink("create new") {
                            setFont(size = 18.0)
                            setOnAction { stage.create() }
                        }
                        text("or") { setFont(size = 18.0) }
                        hyperlink("open existing") {
                            setFont(size = 18.0)
                            setOnAction { stage.open() }
                        }
                    }
                } anchor 0
            } vpriority ALWAYS
        })
        setMinSize(500.0, 700.0)
        setSize(500.0, 700.0)
    }.show()

    private fun Stage.create() = fileChooser(PACKR_FILE_FILTER).showSaveDialog(this)?.let {
        it.writeText(Gson().toJson(PackrItem()))
        it.addToTab()
        tabPane.selectionModel.select(tabs.lastIndex)
    }

    private fun Stage.open() = fileChooser(PACKR_FILE_FILTER).showOpenMultipleDialog(this)?.let {
        it.forEach { it.addToTab() }
        tabPane.selectionModel.select(tabs.lastIndex)
    }

    private fun File.addToTab() {
        tabs.add(packrTab(this) {
            contextMenu = kotfx.scene.contextMenu {
                separatorMenuItem()
                menuItem("Close tab") { setOnAction { tabs.remove(this@packrTab) } }
                menuItem("Delete other tabs") {
                    disableProperty().bind(tabs.sizeBinding lessEq 1)
                    setOnAction {
                        tabs.removeAll(tabs.toMutableList().apply { remove(this@packrTab) })
                    }
                }
                menuItem("Delete tabs to the right") {
                    disableProperty().bind(booleanBindingOf(tabs) { tabs.indexOf(this@packrTab) == tabs.lastIndex })
                    setOnAction {
                        tabs.removeAll(tabs.toList().takeLast(tabs.lastIndex - tabs.indexOf(this@packrTab)))
                    }
                }
            }
        })
    }

    private val tabs: ObservableList<Tab> get() = this@App.tabPane.tabs
}