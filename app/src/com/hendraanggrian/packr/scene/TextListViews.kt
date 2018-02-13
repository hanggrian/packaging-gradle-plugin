@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr.scene

import kotfx.annotations.SceneDsl
import kotfx.scene.ChildManager
import kotfx.scene.ItemManager

inline fun textListView(
    hint: String,
    canBrowseFiles: Boolean = false,
    canBrowseDirectory: Boolean = false,
    extension: String? = null,
    noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseFiles, canBrowseDirectory, extension).apply { init?.invoke(this) }

inline fun ChildManager.textListView(
    hint: String,
    canBrowseFiles: Boolean = false,
    canBrowseDirectory: Boolean = false,
    extension: String? = null,
    noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseFiles, canBrowseDirectory, extension).apply { init?.invoke(this) }.add()

inline fun ItemManager.textListView(
    hint: String,
    canBrowseFiles: Boolean = false,
    canBrowseDirectory: Boolean = false,
    extension: String? = null,
    noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseFiles, canBrowseDirectory, extension).apply { init?.invoke(this) }.add()