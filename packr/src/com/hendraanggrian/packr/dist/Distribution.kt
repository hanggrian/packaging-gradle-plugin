package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Project

open class Distribution(val platform: Platform, project: Project) {

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE.
     * Must be defined or else will throw [NullPointerException].
     */
    var jdkDir: String? = null

    /**
     * The output name.
     * Default is project's name.
     */
    var name: String = project.name

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: MutableList<String> = mutableListOf()
}