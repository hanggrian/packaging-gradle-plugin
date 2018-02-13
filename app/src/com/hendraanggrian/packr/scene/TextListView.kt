package com.hendraanggrian.packr.scene

import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode.MULTIPLE
import javafx.scene.control.cell.TextFieldListCell.forListView
import kotfx.bindings.isEmpty
import kotfx.dialogs.directoryChooser
import kotfx.dialogs.fileChooser
import kotfx.dialogs.inputDialog
import kotfx.scene.menuItem
import kotfx.scene.separatorMenuItem

class TextListView(
    desc: String,
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
            menuItem("Browse files") {
                setOnAction {
                    (if (extension != null) fileChooser(desc, "*.$extension") else fileChooser())
                        .showOpenMultipleDialog(this@TextListView.scene.window)
                        ?.map { it.path }
                        ?.let {
                            this@TextListView.items.addAll(it)
                            this@TextListView.items.sort()
                        }
                }
            }
            if (canBrowseDirectory) menuItem("Browse directory") {
                setOnAction {
                    directoryChooser()
                        .showDialog(this@TextListView.scene.window)
                        ?.let {
                            this@TextListView.items.add(it.path)
                            this@TextListView.items.sort()
                        }
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
}