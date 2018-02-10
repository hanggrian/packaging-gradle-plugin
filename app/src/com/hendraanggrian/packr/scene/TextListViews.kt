@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr.scene

import kotfx.annotations.SceneDsl
import kotfx.scene.ChildRoot
import kotfx.scene.ItemRoot

inline fun textListView(
        hint: String,
        canBrowseFiles: Boolean = false,
        canBrowseDirectory: Boolean = false,
        extension: String? = null,
        noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseFiles, canBrowseDirectory, extension).apply { init?.invoke(this) }

inline fun ChildRoot.textListView(
        hint: String,
        canBrowseFiles: Boolean = false,
        canBrowseDirectory: Boolean = false,
        extension: String? = null,
        noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseFiles, canBrowseDirectory, extension).apply { init?.invoke(this) }.add()

inline fun ItemRoot.textListView(
        hint: String,
        canBrowseFiles: Boolean = false,
        canBrowseDirectory: Boolean = false,
        extension: String? = null,
        noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseFiles, canBrowseDirectory, extension).apply { init?.invoke(this) }.add()