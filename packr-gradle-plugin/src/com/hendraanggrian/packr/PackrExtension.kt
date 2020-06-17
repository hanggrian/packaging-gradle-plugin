@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke
import java.io.File

/** Extension class to be invoked when `packr { ... }` is defined within project. */
open class PackrExtension(
    private val projectName: String,
    private val projectGroup: String,
    override val projectDir: File
) : PackrConfiguration {

    companion object {
        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    internal val distributions: MutableSet<Distribution> = mutableSetOf()

    override var executable: String = projectName
    override var classpath: Iterable<File> = emptyList()
    override var removePlatformLibs: Iterable<File> = emptyList()
    override var mainClass: String? = null
    override var vmArgs: Iterable<String> = emptyList()
    override var resources: Iterable<File> = emptyList()
    override var minimizeJre: String = MINIMIZATION_SOFT
    override lateinit var outputDir: File
    override var cacheJreDir: File? = null
    override var isVerbose: Boolean = false
    override var isAutoOpen: Boolean = false

    /** Enable macOS distribution with default configuration. */
    fun configureMacOS() {
        distributions += MacOSDistribution(projectName, projectGroup, projectDir)
    }

    /**
     * Enable macOS distribution with customized [configuration].
     * Unlike other distributions, macOS configuration have some OS-specific properties.
     */
    fun configureMacOS(configuration: Action<MacOSDistribution>) {
        distributions += MacOSDistribution(projectName, projectGroup, projectDir)
            .also { configuration(it) }
    }

    /**
     * Enable macOS distribution with customized [configuration] in Kotlin DSL.
     * Unlike other distributions, macOS configuration have some OS-specific properties.
     */
    inline fun macOS(noinline configuration: (@DistributionDslMarker MacOSDistribution).() -> Unit): Unit =
        configureMacOS(configuration)

    /** Enable Windows 32-bit distribution with default configuration. */
    fun configureWindows32() {
        distributions += Distribution(PackrConfig.Platform.Windows32, projectName)
    }

    /** Enable Windows 32-bit distribution with customized [configuration]. */
    fun configureWindows32(configuration: Action<Distribution>) {
        distributions += Distribution(PackrConfig.Platform.Windows32, projectName)
            .also { configuration(it) }
    }

    /** Enable Windows 32-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun windows32(noinline configuration: (@DistributionDslMarker Distribution).() -> Unit): Unit =
        configureWindows32(configuration)

    /** Enable Windows 64-bit distribution with default configuration. */
    fun configureWindows64() {
        distributions += Distribution(PackrConfig.Platform.Windows64, projectName)
    }

    /** Enable Windows 64-bit distribution with customized [configuration]. */
    fun configureWindows64(configuration: Action<Distribution>) {
        distributions += Distribution(PackrConfig.Platform.Windows64, projectName)
            .also { configuration(it) }
    }

    /** Enable Windows 64-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun windows64(noinline configuration: (@DistributionDslMarker Distribution).() -> Unit): Unit =
        configureWindows64(configuration)

    /** Enable Linux 32-bit distribution with default configuration. */
    fun configureLinux32() {
        distributions += Distribution(PackrConfig.Platform.Linux32, projectName)
    }

    /** Enable Linux 32-bit distribution with customized [configuration]. */
    fun configureLinux32(configuration: Action<Distribution>) {
        distributions += Distribution(PackrConfig.Platform.Linux32, projectName)
            .also { configuration(it) }
    }

    /** Enable Linux 32-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun linux32(noinline configuration: (@DistributionDslMarker Distribution).() -> Unit): Unit =
        configureLinux32(configuration)

    /** Enable Linux 64-bit distribution with default configuration. */
    fun configureLinux64() {
        distributions += Distribution(PackrConfig.Platform.Linux64, projectName)
    }

    /** Enable Linux 64-bit distribution with customized [configuration]. */
    fun configureLinux64(configuration: Action<Distribution>) {
        distributions += Distribution(PackrConfig.Platform.Linux64, projectName)
            .also { configuration(it) }
    }

    /** Enable Linux 64-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun linux64(noinline configuration: (@DistributionDslMarker Distribution).() -> Unit): Unit =
        configureLinux64(configuration)
}
