package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.VMArged
import org.gradle.api.Project

/**
 * @param platform target platform.
 * Must be defined or else will throw [NullPointerException].
 */
open class Distribution(project: Project, val platform: Platform) : VMArged {

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    /**
     * The output name.
     * Default is project's name.
     */
    var name: String = project.name

    override var vmArgs: MutableCollection<String> = mutableListOf()
}