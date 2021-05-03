@file:Suppress("NOTHING_TO_INLINE", "UnstableApiUsage")

package com.hendraanggrian.packaging

import com.badlogicgames.packr.PackrConfig
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.distribution.Distribution
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import java.io.File

/** Extension class to be invoked when `packr { ... }` is defined within project. */
open class PackagingExtension(
    private val project: Project,
    application: JavaApplication,
    distribution: Distribution
) : PackagingGlobalConfiguration {
    companion object {
        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    internal class PlatformConfiguration(project: Project, val platform: PackrConfig.Platform) :
        PackagingPlatformConfiguration {

        override val releaseName: Property<String> = project.objects.property<String>()
            .convention("${project.name}-${platform.name}")

        override val jdk: Property<String> = project.objects.property<String>()
            .convention(System.getenv("JAVA_HOME") ?: System.getProperty("java.home"))

        override val icon: Property<File> = project.objects.property()

        override val bundleId: Property<String> = project.objects.property()

        override val vmArgs: ListProperty<String> = project.objects.listProperty<String>()
            .convention(mutableListOf())

        override fun toString(): String = platform.name
        override fun hashCode(): Int = platform.hashCode()
        override fun equals(other: Any?): Boolean = other != null &&
            other is PlatformConfiguration &&
            other.platform == platform
    }

    internal val platformConfigurations: MutableSet<PlatformConfiguration> = mutableSetOf()

    override val executable: Property<String> = project.objects.property<String>()
        .convention(project.name)

    override val classpath: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("install/${distribution.distributionBaseName.get()}/lib"))

    override val removePlatformLibraries: ListProperty<File> = project.objects.listProperty<File>()
        .convention(mutableListOf())

    override val mainClass: Property<String> = project.objects.property<String>()
        .convention(application.mainClass)

    override val vmArgs: ListProperty<String> = project.objects.listProperty<String>()
        .convention(mutableListOf())

    override val resources: ListProperty<File> = project.objects.listProperty<File>()
        .convention(mutableListOf())

    override val minimizeJre: Property<String> = project.objects.property<String>()
        .convention(MINIMIZATION_SOFT)

    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("install"))

    override val cacheJreDirectory: DirectoryProperty = project.objects.directoryProperty()

    override val verbose: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)

    override val autoOpen: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)

    /** Enable macOS distribution with default configuration. */
    fun configureMacOS() {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.MacOS)
    }

    /**
     * Enable macOS distribution with customized [configuration].
     * Unlike other distributions, macOS configuration have some OS-specific properties.
     */
    fun configureMacOS(configuration: Action<PackagingPlatformConfiguration>) {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.MacOS)
            .also { configuration(it) }
    }

    /**
     * Enable macOS distribution with customized [configuration] in Kotlin DSL.
     * Unlike other distributions, macOS configuration have some OS-specific properties.
     */
    inline fun macOS(noinline configuration: (@PackagingDslMarker PackagingPlatformConfiguration).() -> Unit): Unit =
        configureMacOS(configuration)

    /** Enable Windows 32-bit distribution with default configuration. */
    fun configureWindows32() {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Windows32)
    }

    /** Enable Windows 32-bit distribution with customized [configuration]. */
    fun configureWindows32(configuration: Action<PackagingPlatformConfiguration>) {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Windows32)
            .also { configuration(it) }
    }

    /** Enable Windows 32-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun windows32(noinline configuration: (@PackagingDslMarker PackagingPlatformConfiguration).() -> Unit): Unit =
        configureWindows32(configuration)

    /** Enable Windows 64-bit distribution with default configuration. */
    fun configureWindows64() {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Windows64)
    }

    /** Enable Windows 64-bit distribution with customized [configuration]. */
    fun configureWindows64(configuration: Action<PackagingPlatformConfiguration>) {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Windows64)
            .also { configuration(it) }
    }

    /** Enable Windows 64-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun windows64(noinline configuration: (@PackagingDslMarker PackagingPlatformConfiguration).() -> Unit): Unit =
        configureWindows64(configuration)

    /** Enable Linux 32-bit distribution with default configuration. */
    fun configureLinux32() {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Linux32)
    }

    /** Enable Linux 32-bit distribution with customized [configuration]. */
    fun configureLinux32(configuration: Action<PackagingPlatformConfiguration>) {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Linux32)
            .also { configuration(it) }
    }

    /** Enable Linux 32-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun linux32(noinline configuration: (@PackagingDslMarker PackagingPlatformConfiguration).() -> Unit): Unit =
        configureLinux32(configuration)

    /** Enable Linux 64-bit distribution with default configuration. */
    fun configureLinux64() {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Linux64)
    }

    /** Enable Linux 64-bit distribution with customized [configuration]. */
    fun configureLinux64(configuration: Action<PackagingPlatformConfiguration>) {
        platformConfigurations += PlatformConfiguration(project, PackrConfig.Platform.Linux64)
            .also { configuration(it) }
    }

    /** Enable Linux 64-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun linux64(noinline configuration: (@PackagingDslMarker PackagingPlatformConfiguration).() -> Unit): Unit =
        configureLinux64(configuration)
}
