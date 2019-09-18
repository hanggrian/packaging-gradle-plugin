package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import java.io.File
import org.gradle.api.Project

/** Delimits a distribution DSL in Gradle Kotlin DSL scripts. */
@DslMarker
annotation class PackrDslMarker

@PackrDslMarker
interface DistributionBuilder : VmArged {

    /** File name of this distribution that will be generated. */
    var name: String?

    /** Groovy-friendly method. */
    fun name(value: String) {
        name = value
    }

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String?

    /** Groovy-friendly method. */
    fun jdk(value: String) {
        jdk = value
    }
}

@PackrDslMarker
interface MacOSDistributionBuilder : DistributionBuilder {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: File?

    /** Convenient method to set icon resource from file path, relative to project directory. */
    fun icon(value: String)

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String?

    /** Groovy-friendly method. */
    fun bundleId(value: String) {
        bundleId = value
    }
}

internal open class Distribution(val project: Project, val platform: PackrConfig.Platform) : DistributionBuilder {

    override var name: String? = "${project.name}-$platform"

    override var jdk: String? = System.getenv("JAVA_HOME") ?: System.getProperty("java.home")

    override var vmArgs: Iterable<String> = emptyList()

    override fun hashCode(): Int = platform.hashCode()

    override fun equals(other: Any?): Boolean = other != null && other is Distribution && other.platform == platform
}

internal class MacOSDistribution(project: Project) : Distribution(project, PackrConfig.Platform.MacOS),
    MacOSDistributionBuilder {

    override var icon: File? = null

    override fun icon(value: String) {
        icon = project.projectDir.resolve(value)
    }

    override var bundleId: String? = null
}
