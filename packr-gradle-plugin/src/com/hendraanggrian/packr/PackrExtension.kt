@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr

import java.io.File
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke

open class PackrExtension(private val projectName: String, override val projectDir: File) : PackrConfiguration {

    companion object {
        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    private val distributions: MutableSet<Distribution> = mutableSetOf()

    internal operator fun get(platform: Platform): Distribution? =
        distributions.singleOrNull { it.platform == platform }

    override var executable: String? = null
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
        distributions += MacOSDistribution(projectDir, projectName)
    }

    /**
     * Enable macOS distribution with customized [configuration].
     * Unlike other distributions, macOS configuration have some OS-specific properties.
     */
    fun configureMacOS(configuration: Action<MacOSDistribution>) {
        distributions += MacOSDistribution(projectDir, projectName).also { configuration(it) }
    }

    /**
     * Enable macOS distribution with customized [configuration] in Kotlin DSL.
     * Unlike other distributions, macOS configuration have some OS-specific properties.
     */
    inline fun macOS(noinline configuration: MacOSDistribution.() -> Unit): Unit =
        configureMacOS(configuration)

    /** Enable Windows 32-bit distribution with default configuration. */
    fun configureWindows32() {
        distributions += Distribution(Platform.Windows32, projectName)
    }

    /** Enable Windows 32-bit distribution with customized [configuration]. */
    fun configureWindows32(configuration: Action<Distribution>) {
        distributions += Distribution(Platform.Windows32, projectName).also { configuration(it) }
    }

    /** Enable Windows 32-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun windows32(noinline configuration: Distribution.() -> Unit): Unit =
        configureWindows32(configuration)

    /** Enable Windows 64-bit distribution with default configuration. */
    fun configureWindows64() {
        distributions += Distribution(Platform.Windows64, projectName)
    }

    /** Enable Windows 64-bit distribution with customized [configuration]. */
    fun configureWindows64(configuration: Action<Distribution>) {
        distributions += Distribution(Platform.Windows64, projectName).also { configuration(it) }
    }

    /** Enable Windows 64-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun windows64(noinline configuration: Distribution.() -> Unit): Unit =
        configureWindows64(configuration)

    /** Enable Linux 32-bit distribution with default configuration. */
    fun configureLinux32() {
        distributions += Distribution(Platform.Linux32, projectName)
    }

    /** Enable Linux 32-bit distribution with customized [configuration]. */
    fun configureLinux32(configuration: Action<Distribution>) {
        distributions += Distribution(Platform.Linux32, projectName).also { configuration(it) }
    }

    /** Enable Linux 32-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun linux32(noinline configuration: Distribution.() -> Unit): Unit =
        configureLinux32(configuration)

    /** Enable Linux 64-bit distribution with default configuration. */
    fun configureLinux64() {
        distributions += Distribution(Platform.Linux64, projectName)
    }

    /** Enable Linux 64-bit distribution with customized [configuration]. */
    fun configureLinux64(configuration: Action<Distribution>) {
        distributions += Distribution(Platform.Linux64, projectName).also { configuration(it) }
    }

    /** Enable Linux 64-bit distribution with customized [configuration] in Kotlin DSL. */
    inline fun linux64(noinline configuration: Distribution.() -> Unit): Unit =
        configureLinux64(configuration)
}
