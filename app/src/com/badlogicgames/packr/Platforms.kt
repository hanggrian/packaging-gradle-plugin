/**
 * Platform in packr library has default package visibility modifier,
 * making it inaccessible to package outside this one.
 * This utility class open those properties in public accessors.
 */

package com.badlogicgames.packr

import com.badlogicgames.packr.PackrConfig.Platform

val Platform.desc: String get() = desc

fun String.toPlatform(): Platform = Platform.byDesc(this)
