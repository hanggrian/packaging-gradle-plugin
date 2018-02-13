/**
 * Platform in packr library has default visibility modifier,
 * making it unusable to public packages.
 * This utility class access those properties in public functions.
 */

package com.badlogicgames.packr

val PackrConfig.Platform.desc: String get() = desc

fun String.toPlatform(): PackrConfig.Platform = PackrConfig.Platform.byDesc(this)
