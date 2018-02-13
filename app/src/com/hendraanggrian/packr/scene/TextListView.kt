package com.hendraanggrian.packr.scene

import com.hendraanggrian.packr.withoutLeadingSlash
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode.MULTIPLE
import javafx.scene.control.cell.TextFieldListCell.forListView
import kotfx.bindings.isEmpty
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.errorAlert
import kotfx.dialogs.fileChooser
import kotfx.dialogs.inputDialog
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem
import java.io.File

class TextListView(
    private val initialFile: File,
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
                    (if (extension != null) fileChooser(desc, "*.$extension") else fileChooser())
                        .showOpenMultipleDialog(this@TextListView.scene.window)
                        ?.forEach { it.addToList() }
                }
            }
            if (canBrowseDirectory) menuItem("Browse directory") {
                setOnAction {
                    directoryChooser()
                        .showDialog(this@TextListView.scene.window)
                        ?.addToList()
                }
            }
            separatorMenuItem()
            menuItem("Delete") {
                disableProperty().bind(this@TextListView.selectionModel.selectedItems.isEmpty)
                setOnAction { this@TextListView.selectionModel.selectedIndices.forEach { this@TextListView.items.removeAt(it) } }
            }
            menuItem("Clear") {
                disableProperty().bind(this@TextListView.items.isEmpty)
                setOnAction { this@TextListView.items.clear() }
            }
        }
    }

    private fun File.addToList() = @Suppress("IMPLICIT_CAST_TO_ANY") when {
        path.startsWith(initialFile.parent) -> {
            this@TextListView.items.add(path.substring(initialFile.parent.count()).withoutLeadingSlash)
            this@TextListView.items.sort()
        }
        else -> errorAlert("File is not relative to initial path.").showAndWait()
    }
}