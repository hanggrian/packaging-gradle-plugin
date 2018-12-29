package com.hendraanggrian.packr.dist

internal open class Distribution : DistributionBuilder {

    override var name: String? = null

    override var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    override val vmArgs: MutableCollection<String> = mutableSetOf()
}