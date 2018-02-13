package com.hendraanggrian.packr.gson

data class PackrItem(
    var platform: String? = null,
    var jdk: String? = null,
    var executable: String? = null,
    var classpath: ArrayList<String>? = null,
    var mainclass: String? = null,
    var vmargs: ArrayList<String>? = null,
    var resources: ArrayList<String>? = null,
    var minimizejre: String? = null,
    var output: String? = null
)