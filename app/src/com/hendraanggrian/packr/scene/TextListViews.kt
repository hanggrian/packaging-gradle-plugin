@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr.scene

import kotfx.annotations.SceneDsl
import kotfx.scene.ChildManager

inline fun ChildManager.textListView(
    hint: String,
    canBrowseDirectory: Boolean = false,
    extension: String? = null,
    noinline init: ((@SceneDsl TextListView).() -> Unit)? = null
): TextListView = TextListView(hint, canBrowseDirectory, extension).apply { init?.invoke(this) }.add()