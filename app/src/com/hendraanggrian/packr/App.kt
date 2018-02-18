package com.hendraanggrian.packr

import com.google.gson.Gson
import com.hendraanggrian.packr.BuildConfig.NAME
import com.hendraanggrian.packr.BuildConfig.VERSION
import com.hendraanggrian.packr.scene.packrTab
import javafx.application.Application
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos.CENTER_LEFT
import javafx.geometry.VPos.TOP
import javafx.scene.Scene
import javafx.scene.control.ButtonType.CLOSE
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
import kotfx.dialogs.addButton
import kotfx.dialogs.content
import kotfx.dialogs.dialog
import kotfx.dialogs.fileChooser
import kotfx.dialogs.graphicIcon
import kotfx.dialogs.headerTitle
import kotfx.dialogs.icon
import kotfx.exit
import kotfx.gap
import kotfx.icon
import kotfx.maxSize
import kotfx.scene.anchorPane
import kotfx.scene.borderPane
import kotfx.scene.button
import kotfx.scene.hbox
import kotfx.scene.hyperlink
import kotfx.scene.imageView
import kotfx.scene.label
import kotfx.scene.loadFont
import kotfx.scene.menu
import kotfx.scene.menuBar
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem
import kotfx.scene.setFont
import kotfx.scene.tabPane
import kotfx.scene.text
import kotfx.scene.textFlow
import kotfx.scene.vbox
import kotfx.setMinSize
import kotfx.setSize
import java.awt.Desktop.getDesktop
import java.io.File
import java.net.URI

/** Main Packr application. */
class App : Application() {

    companion object {
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
                    separatorMenuItem()
                    menuItem("Quit Packr") { setOnAction { exit() } }
                }
                menu("Help") {
                    menuItem("How-to") {
                        setOnAction {
                            dialog<Unit> {
                                headerTitle = "How-to"
                                graphicIcon = ImageView(R.image.logo_packr).apply {
                                    fitWidth = 72.0
                                    fitHeight = 72.0
                                }
                                content = kotfx.scene.gridPane {
                                    gap = 8.0
                                    label("1.") row 0 col 0 vpos TOP
                                    textFlow {
                                        text("JSON file containing $NAME configuration is required to open a tab.\nCreate a new one or load existing file by selecting ") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("File") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(" menu.") { loadFont(R.font.lato_regular.form, 14.0) }
                                    } row 0 col 1
                                    label("2.") row 1 col 0 vpos TOP
                                    textFlow {
                                        text("Fill out the forms accordingly.\n") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("Resources") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(", ") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("Mac icon") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(", ") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("Mac bundle") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(" are optional.\n") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("Hover the box") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(" to display description.")
                                    } row 1 col 1
                                    label("3.") row 2 col 0 vpos TOP
                                    textFlow {
                                        text("Click ") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("Save") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(" to update JSON file, or ") { loadFont(R.font.lato_regular.form, 14.0) }
                                        text("Process") { loadFont(R.font.lato_bold.form, 14.0) }
                                        text(" start packing.") { loadFont(R.font.lato_regular.form, 14.0) }
                                    } row 2 col 1
                                }
                                addButton(CLOSE)
                            }.showAndWait()
                        }
                    }
                    separatorMenuItem()
                    menuItem("About", ImageView(R.image.menu_about)) {
                        setOnAction {
                            dialog<Unit>(text) {
                                icon = Image(R.image.ic_about)
                                content = kotfx.scene.hbox {
                                    padding = Insets(48.0)
                                    imageView(Image(R.image.logo_packr))
                                    vbox {
                                        alignment = CENTER_LEFT
                                        textFlow {
                                            text("Packr ") { loadFont(R.font.lato_bold.form, 24.0) }
                                            text("GUI ") { loadFont(R.font.lato_light.form, 24.0) }
                                        }
                                        text("Version $VERSION") { loadFont(R.font.lato_regular.form, 12.0) } marginTop 2
                                        text("Built using open-source software.") { loadFont(R.font.lato_bold.form, 12.0) } marginTop 20
                                        textFlow {
                                            text("Powered by ") { loadFont(R.font.lato_bold.form, 12.0) }
                                            hyperlink("packr") {
                                                loadFont(R.font.lato_regular.form, 12.0)
                                                setOnAction { getDesktop().browse(URI("https://github.com/libgdx/packr")) }
                                            }
                                        } marginTop 4
                                        textFlow {
                                            text("Author ") { loadFont(R.font.lato_bold.form, 12.0) }
                                            hyperlink("Hendra Anggrian") {
                                                loadFont(R.font.lato_regular.form, 12.0)
                                                setOnAction { getDesktop().browse(URI("https://github.com/hendraanggrian")) }
                                            }
                                        }
                                        hbox {
                                            button("GitHub") { setOnAction { getDesktop().browse(URI("https://github.com/hendraanggrian/packr-gui")) } } marginRight 8
                                            button("Check for updates") { setOnAction { getDesktop().browse(URI("https://github.com/hendraanggrian/packr-gui/releases")) } }
                                        } marginTop 20
                                    } marginLeft 48
                                    addButton(CLOSE)
                                }
                            }.showAndWait()
                        }
                    }
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

    private fun Stage.create() = fileChooser(packrFileExtension).showSaveDialog(this)?.let {
        it.writeText(Gson().toJson(PackrItem()))
        it.addToTab()
        tabPane.selectionModel.select(tabs.lastIndex)
    }

    private fun Stage.open() = fileChooser(packrFileExtension).showOpenMultipleDialog(this)?.let {
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

    private inline val String.form: String get() = App::class.java.getResource(this).toExternalForm()

    private inline val packrFileExtension get() = FileChooser.ExtensionFilter("Packr configuration file", "*.json")

    private inline val tabs: ObservableList<Tab> get() = this@App.tabPane.tabs
}
