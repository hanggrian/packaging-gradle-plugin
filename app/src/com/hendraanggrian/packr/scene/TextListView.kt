package com.hendraanggrian.packr.scene

import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode.MULTIPLE
import javafx.scene.control.cell.TextFieldListCell.forListView
import javafx.stage.FileChooser.ExtensionFilter
import kotfx.bindings.isEmpty
import kotfx.coroutines.onAction
import kotfx.coroutines.onEditCommit
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.fileChooser
import kotfx.dialogs.inputDialog
import kotfx.layout.menuItem
import kotfx.layout.separatorMenuItem
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
        onEditCommit {
            items[it.index] = it.newValue
            items.sort()
        }
        contextMenu = kotfx.layout.contextMenu {
            menuItem("Add") {
                onAction {
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
                onAction {
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
                onAction {
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
                onAction { this@TextListView.selectionModel.selectedIndices.forEach { this@TextListView.items.removeAt(it) } }
            }
            menuItem("Clear") {
                disableProperty().bind(this@TextListView.items.isEmpty)
                onAction { this@TextListView.items.clear() }
            }
        }
    }
}
