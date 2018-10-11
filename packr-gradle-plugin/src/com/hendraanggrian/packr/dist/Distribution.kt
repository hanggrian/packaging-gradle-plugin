package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.VmArged

open class Distribution(val platform: Platform, var name: String) : VmArged {

    /** Groovy-specific method to change name. */
    fun name(name: String) {
        this.name = name
    }

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    /** Groovy-specific method to change jdk path. */
    fun jdk(jdk: String) {
        this.jdk = jdk
    }

    override val vmArgs: MutableCollection<String> = mutableListOf()
}