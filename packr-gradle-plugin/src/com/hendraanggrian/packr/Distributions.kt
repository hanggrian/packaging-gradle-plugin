package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import java.io.File
import java.io.Serializable

/**
 * Represents a platform-specific configuration.
 *
 * @param platform target platform of this distribution.
 * @param name file name of this distribution that will be generated. Default is project's name.
 * @param jdk directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build this distribution. Default is Java Home environment variable, if any.
 */
open class Distribution(
    val platform: PackrConfig.Platform,
    var name: String,
    var jdk: String? = javaHome
) : VmArged, Serializable {

    override var vmArgs: Iterable<String> = emptyList()
    override fun hashCode(): Int = platform.hashCode()
    override fun equals(other: Any?): Boolean = other != null && other is Distribution && other.platform == platform
    override fun toString(): String = platform.toString()
}

/**
 * Represents a macOS distribution, providing extra properties only relevant in macOS.
 *
 * @param projectName file name of this distribution that will be generated. Default is project's name.
 * @param projectGroup project group name to be used as default [bundleId].
 * @param projectDir working directory of [org.gradle.api.Project] needed to point relative paths.
 * @param jdk directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build this distribution. Default is Java Home environment variable, if any.
 * @param icon Location of an AppBundle icon resource (.icns file) relative to project directory. This is an optional property.
 * @param bundleId The bundle identifier of your Java application, e.g. `com.my.app`. This is an optional property.
 */
class MacOSDistribution(
    projectName: String,
    projectGroup: Any,
    private val projectDir: File,
    jdk: String? = javaHome,
    var icon: File? = null,
    var bundleId: String? = projectGroup.toString()
) : Distribution(PackrConfig.Platform.MacOS, projectName, jdk) {

    /** Convenient method to set icon resource from file path, relative to project directory. */
    fun icon(relativePath: String) {
        icon = projectDir.resolve(relativePath)
    }
}

/** Java home system environment of this system, if any. */
private val javaHome: String? get() = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")
