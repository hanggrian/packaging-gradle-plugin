package com.badlogicgames.packr

val PackrConfig.Platform.desc: String get() = desc

fun String.toPlatform(): PackrConfig.Platform = PackrConfig.Platform.byDesc(this)