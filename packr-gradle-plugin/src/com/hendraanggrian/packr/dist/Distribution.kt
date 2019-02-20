package com.hendraanggrian.packr.dist

import org.gradle.api.Project

internal open class Distribution(override val project: Project) : DistributionBuilder {

    override var name: String? = null

    override var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    override var vmArgs: Iterable<String> = emptyList()
}