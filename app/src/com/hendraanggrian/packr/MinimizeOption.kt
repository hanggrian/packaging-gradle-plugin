package com.hendraanggrian.packr

enum class MinimizeOption {
    Soft,
    Hard,
    OracleJRE8;

    val value: String get() = toString().toLowerCase()
}