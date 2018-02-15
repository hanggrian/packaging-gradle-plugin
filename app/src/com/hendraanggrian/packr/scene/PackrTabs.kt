@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr.scene

import kotfx.annotations.SceneDsl
import java.io.File

inline fun packrTab(
    jsonFile: File,
    noinline init: ((@SceneDsl PackrTab).() -> Unit)? = null
): PackrTab = PackrTab(jsonFile).apply { init?.invoke(this) }