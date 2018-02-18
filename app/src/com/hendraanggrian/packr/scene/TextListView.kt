package com.hendraanggrian.packr.scene

import com.hendraanggrian.packr.minus
import com.hendraanggrian.packr.relativeTo
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode.MULTIPLE
import javafx.scene.control.cell.TextFieldListCell.forListView
import javafx.stage.FileChooser.ExtensionFilter
import kotfx.bindings.isEmpty
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.fileChooser
import kotfx.dialogs.inputDialog
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem
import java.io.File

/** ListView containing editable text and options to add and browse through context menus. */
class TextListView(
    private val jsonFile: File,
    desc: String,
    canBrowseFile: Boolean,
    canBrowseDirectory: Boolean,
    extension: String?
) : ListView<String>() {

    init {
        selectionModel.selectionMode = MULTIPLE
        isEditable = true
        cellFactory = forListView()
        setOnEditCommit {
            items[it.index] = it.newValue
            items.sort()
        }
        contextMenu = kotfx.scene.contextMenu {
            menuItem("Add") {
                setOnAction {
                    inputDialog {
                        contentText = desc
                        editor.promptText = desc
                    }.showAndWait().ifPresent {
                        this@TextListView.items.add(it)
                        this@TextListView.items.sort()
                    }
                }
            }
            if (canBrowseFile) menuItem("Browse files") {
                setOnAction {
                    (if (extension != null) fileChooser(jsonFile.parentFile, null, ExtensionFilter(desc, "*.$extension")) else fileChooser(jsonFile.parentFile, null))
                        .showOpenMultipleDialog(this@TextListView.scene.window)
                        ?.let { files ->
                            when {
                                files.all { it relativeTo jsonFile } -> {
                                    this@TextListView.items.addAll(files.map { it - jsonFile })
                                    this@TextListView.items.sort()
                                }
                                else -> errorAlert("File is not relative to initial path.").showAndWait()
                            }
                        }
                }
            }
            if (canBrowseDirectory) menuItem("Browse directory") {
                setOnAction {
                    directoryChooser(jsonFile.parentFile)
                        .showDialog(this@TextListView.scene.window)
                        ?.let { file ->
                            when {
                                file relativeTo jsonFile -> {
                                    this@TextListView.items.add(file - jsonFile)
                                    this@TextListView.items.sort()
                                }
                                else -> errorAlert("File is not relative to initial path.").showAndWait()
                            }
                        }
                }
            }
            separatorMenuItem()
            menuItem("Remove") {
                disableProperty().bind(this@TextListView.selectionModel.selectedItems.isEmpty)
                setOnAction { this@TextListView.selectionModel.selectedIndices.forEach { this@TextListView.items.removeAt(it) } }
            }
            menuItem("Clear") {
                disableProperty().bind(this@TextListView.items.isEmpty)
                setOnAction { this@TextListView.items.clear() }
            }
        }
    }
}
