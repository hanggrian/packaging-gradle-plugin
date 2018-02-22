package com.hendraanggrian.packr.scene

import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode.MULTIPLE
import javafx.stage.FileChooser.ExtensionFilter
import kotfx.bindings.isEmpty
import kotfx.coroutines.onAction
import kotfx.coroutines.onEditCommit
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.fileChooser
import kotfx.dialogs.inputDialog
import kotfx.layout.contextMenu
import kotfx.layout.menuItem
import kotfx.layout.separatorMenuItem
import kotfx.textFieldCellFactory
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
        textFieldCellFactory()
        onEditCommit {
            items[it.index] = it.newValue
            items.sort()
        }
        contextMenu {
            menuItem("Add") {
                onAction {
                    inputDialog {
                        contentText = desc
                        editor.promptText = desc
                    }.showAndWait().ifPresent {
                        texts += it
                        texts.sort()
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
                                    texts += files.map { it - jsonFile }
                                    texts.sort()
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
                                    texts -= file - jsonFile
                                    texts.sort()
                                }
                                else -> errorAlert("File is not relative to initial path.").showAndWait()
                            }
                        }
                }
            }
            separatorMenuItem()
            menuItem("Remove") {
                disableProperty().bind(selectionModel.selectedItems.isEmpty)
                onAction { selectionModel.selectedIndices.forEach { texts.removeAt(it) } }
            }
            menuItem("Clear") {
                disableProperty().bind(texts.isEmpty)
                onAction { texts.clear() }
            }
        }
    }

    private val texts: ObservableList<String> get() = items
}
