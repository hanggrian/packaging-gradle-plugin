@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr.scene

import kotfx.annotations.LayoutDsl
import kotfx.layout.ChildManager
import java.io.File

inline fun packrTab(
    jsonFile: File,
    noinline init: ((@LayoutDsl PackrTab).() -> Unit)? = null
): PackrTab = PackrTab(jsonFile).apply { init?.invoke(this) }

inline fun ChildManager.textListView(
    jsonFile: File,
    hint: String,
    canBrowseFile: Boolean = false,
    canBrowseDirectory: Boolean = false,
    extension: String? = null,
    noinline init: ((@LayoutDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(jsonFile, hint, canBrowseFile, canBrowseDirectory, extension).apply { init?.invoke(this) }.add()