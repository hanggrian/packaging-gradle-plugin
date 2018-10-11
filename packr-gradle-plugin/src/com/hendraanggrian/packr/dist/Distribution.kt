package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.VMArged

open class Distribution(var name: String, val platform: Platform) : VMArged {

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    override var vmArgs: MutableCollection<String> = mutableListOf()
}