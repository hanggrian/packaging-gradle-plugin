package com.hendraanggrian.packr

import java.io.File

/**
 * Delimits a distribution DSL in Gradle Kotlin DSL scripts.
 *
 * @see [DslMarker]
 */
@DslMarker
@Target(AnnotationTarget.TYPE)
annotation class DistributionDsl

sealed class Distribution : VmArged {

    /** File name of this distribution that will be generated. */
    var name: String? = null

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

    override val vmArgs: MutableCollection<String> = mutableSetOf()
}

class SimpleDistribution : Distribution()

class MacOSDistribution : Distribution() {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: File? = null

    /** Groovy-specific method to change icon path. */
    fun icon(icon: File) {
        this.icon = icon
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null

    /** Groovy-specific method to change bundle identifier. */
    fun bundleId(bundleId: String) {
        this.bundleId = bundleId
    }
}