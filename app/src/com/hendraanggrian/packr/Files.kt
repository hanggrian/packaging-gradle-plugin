@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr

import java.io.File
import java.io.File.separatorChar

inline infix fun File.relativeTo(jsonFile: File): Boolean = path.startsWith(jsonFile.parent)

inline operator fun File.minus(jsonFile: File): String = path.substring(jsonFile.parent.count()).let {
    if (it.startsWith(separatorChar)) it.substring(1) else it
}