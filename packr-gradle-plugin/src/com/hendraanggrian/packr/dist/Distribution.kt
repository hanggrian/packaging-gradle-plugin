package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.internal.VMArged
import org.gradle.api.Project

/**
 * @param platform target platform.
 * @param jdk Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE.
 * Must be defined or else will throw [NullPointerException].
 */
open class Distribution(
    val platform: Platform,
    project: Project,
    val jdk: String
) : VMArged {

    /**
     * The output name.
     * Default is project's name.
     */
    var name: String = project.name

    override var vmArgs: MutableList<String> = mutableListOf()
}