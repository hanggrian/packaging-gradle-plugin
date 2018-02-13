package com.hendraanggrian.packr

import com.google.gson.Gson
import com.hendraanggrian.packr.gson.PackrItem
import com.hendraanggrian.packr.scene.packrTab
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotfx.bindings.booleanBindingOf
import kotfx.bindings.lessEq
import kotfx.bindings.sizeBinding
import kotfx.dialogs.fileChooser
import kotfx.scene.menu
import kotfx.scene.menuBar
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem
import kotfx.scene.tabPane
import kotfx.scene.vbox
import kotfx.setMinSize
import kotfx.setSize
import java.io.File

class App : Application() {

    companion object {
        private val PACKR_FILE_FILTER = FileChooser.ExtensionFilter("Packr configuration file", "*.json")

        @JvmStatic fun main(args: Array<String>) = Application.launch(App::class.java, *args)
    }

    private lateinit var tabPane: TabPane

    override fun start(stage: Stage) = stage.apply {
        title = "Packr"
        scene = Scene(vbox {
            menuBar {
                isUseSystemMenuBar = true
                menu("File") {
                    menuItem("New") {
                        setOnAction {
                            fileChooser(PACKR_FILE_FILTER).showSaveDialog(stage)?.let {
                                it.writeText(Gson().toJson(PackrItem()))
                                it.addToTab()
                            }
                        }
                    }
                    menuItem("Open...", ImageView(R.image.menu_folder)) {
                        setOnAction {
                            fileChooser(PACKR_FILE_FILTER).showOpenMultipleDialog(stage)?.forEach {
                                it.addToTab()
                            }
                        }
                    }
                }
            }
            tabPane = tabPane()
        })
        setMinSize(500.0, 700.0)
        setSize(500.0, 700.0)
    }.show()

    private fun File.addToTab() {
        val tabs = this@App.tabPane.tabs
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
}