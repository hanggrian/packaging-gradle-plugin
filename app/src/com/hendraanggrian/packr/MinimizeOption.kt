package com.hendraanggrian.packr

/**
 * Enumeration representing Packr minimize JRE option with
 * [com.badlogicgames.packr.PackrConfig.Platform] convention.
 */
enum class MinimizeOption {
    Soft,
    Hard,
    OracleJRE8;

    val desc: String get() = toString().toLowerCase()

    companion object {
        fun byDesc(desc: String): MinimizeOption = values().first { it.desc.equals(desc, true) }
    }
}
