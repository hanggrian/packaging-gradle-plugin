package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import com.hendraanggrian.packr.dist.Distribution
import com.hendraanggrian.packr.dist.DistributionDsl
import com.hendraanggrian.packr.dist.MacOSDistribution
import java.io.File

open class PackrExtension : VmArged {

    companion object {
        const val MINIMIZE_SOFT = "soft"
        const val MINIMIZE_HARD = "hard"
        const val MINIMIZE_ORACLEJRE8 = "oraclejre8"
    }

    internal val distributions = mutableListOf<Distribution>()

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    var executable: String = ""

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    val classpath: MutableList<String> = mutableListOf()

    /** Groovy-specific method to add classpath. */
    fun classpath(vararg classpath: String) {
        this.classpath += classpath
    }

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Must be defined or will throw an exception.
     */
    var mainClass: String = ""

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    val resources: MutableList<File> = mutableListOf()

    /** Groovy-specific method to add resources. */
    fun resources(vararg resources: File) {
        this.resources += resources
    }

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is `soft`.
     */
    var minimizeJre: String = MINIMIZE_SOFT

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    lateinit var outputDir: File

    override val vmArgs: MutableCollection<String> = mutableSetOf()

    /**
     * Print extra messages about JRE minimizeJre when set to `true`.
     * This is an optional property.
     */
    var verbose: Boolean = false

    /**
     * Open [outputDir] upon packing completion.
     * This is an optional property.
     */
    var openOnDone: Boolean = false

    /** Configure macOS distribution. Unlike other distributions, mac configuration have some OS-specific properties. */
    @JvmOverloads fun macOS(init: ((@DistributionDsl MacOSDistribution).() -> Unit)? = null) {
        distributions += MacOSDistribution().also { init?.invoke(it) }
    }

    /** Configure Windows 32-bit distribution. */
    @JvmOverloads fun windows32(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions += Distribution(PackrConfig.Platform.Windows32).also { init?.invoke(it) }
    }

    /** Configure Windows 64-bit distribution. */
    @JvmOverloads fun windows64(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions += Distribution(PackrConfig.Platform.Windows64).also { init?.invoke(it) }
    }

    /** Configure Linux 32-bit distribution. */
    @JvmOverloads fun linux32(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions += Distribution(PackrConfig.Platform.Linux32).also { init?.invoke(it) }
    }

    /** Configure Linux 64-bit distribution. */
    @JvmOverloads fun linux64(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions += Distribution(PackrConfig.Platform.Linux64).also { init?.invoke(it) }
    }
}