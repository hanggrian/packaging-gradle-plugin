package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Project

open class Distribution(val platform: Platform, project: Project) {

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE.
     * Must be defined or else will throw [NullPointerException].
     */
    var jdk: String? = null

    /** Alias for Groovy initialization without operator. */
    fun jdk(jdk: String?) {
        this.jdk = jdk
    }

    /**
     * The output name.
     * Default is project's name.
     */
    var name: String = project.name

    /** Alias for Groovy initialization without operator. */
    fun name(name: String) {
        this.name = name
    }

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: MutableList<String> = mutableListOf()
}