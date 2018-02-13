@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr.scene

import kotfx.annotations.SceneDsl
import java.io.File

inline fun packrTab(
    initialFile: File,
    noinline init: ((@SceneDsl PackrTab).() -> Unit)? = null
): PackrTab = PackrTab(initialFile).apply { init?.invoke(this) }