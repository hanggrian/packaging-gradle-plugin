package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import java.io.File
import java.io.Serializable

/** Delimits a distribution DSL in Gradle Kotlin DSL scripts. */
@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class PackrDslMarker

/**
 * Represents a platform-specific configuration.
 *
 * @param name file name of this distribution that will be generated.
 * @param platform target platform
 */
@PackrDslMarker
open class Distribution(var name: String, val platform: PackrConfig.Platform) : VmArged, Serializable {

    /** Groovy-friendly method to set distribution name. */
    fun name(distributionName: String) {
        name = distributionName
    }

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    /** Groovy-friendly method to set JDK path. */
    fun jdk(path: String) {
        jdk = path
    }

    /**
     * To avoid overwriting distributions with the same name,
     * the output name will have a platform suffix separated by dash.
     */
    val outputName: String = "$name-$platform"

    override var vmArgs: Iterable<String> = emptyList()
    override fun hashCode(): Int = platform.hashCode()
    override fun equals(other: Any?): Boolean = other != null && other is Distribution && other.platform == platform
}

/**
 * Represents a macOS distribution configuration, providing extra properties only relevant in macOS.
 *
 * @param name file name of this distribution that will be generated.
 * @param projectDir working directory of [org.gradle.api.Project] needed to point relative paths.
 */
@PackrDslMarker
class MacOSDistribution(name: String, private val projectDir: File) : Distribution(name, PackrConfig.Platform.MacOS) {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: File? = null

    /** Convenient method to set icon resource from file path, relative to project directory. */
    fun icon(relativePath: String) {
        icon = projectDir.resolve(relativePath)
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null

    /** Groovy-friendly method to set bundle identifier. */
    fun bundleId(id: String) {
        bundleId = id
    }
}
