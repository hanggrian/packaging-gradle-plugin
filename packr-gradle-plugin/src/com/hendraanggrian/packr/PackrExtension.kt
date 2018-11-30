package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig

open class PackrExtension : VmArged {

    companion object {
        const val MINIMIZE_SOFT = "soft"
        const val MINIMIZE_HARD = "hard"
        const val MINIMIZE_ORACLEJRE8 = "oraclejre8"
    }

    internal val distributions = mutableMapOf<PackrConfig.Platform, Distribution>()

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    var executable: String? = null

    /** Groovy-specific method to set executable. */
    fun executable(executable: String) {
        this.executable = executable
    }

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
    var mainClass: String? = null

    /** Groovy-specific method to set main class. */
    fun mainClass(mainClass: String) {
        this.mainClass = mainClass
    }

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    val resources: MutableList<String> = mutableListOf()

    /** Groovy-specific method to add resources. */
    fun resources(vararg resources: String) {
        this.resources += resources
    }

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is `soft`.
     */
    var minimizeJre: String = MINIMIZE_SOFT

    /** Groovy-specific method to minimize jre. */
    fun minimizeJre(minimizeJre: String) {
        this.minimizeJre = minimizeJre
    }

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    lateinit var outputDirectory: String

    /** Groovy-specific method to change output directory. */
    fun outputDirectory(outputDirectory: String) {
        this.outputDirectory = outputDirectory
    }

    override val vmArgs: MutableCollection<String> = mutableSetOf()

    /**
     * Print extra messages about JRE minimizeJre when set to `true`.
     * This is an optional property.
     */
    var verbose: Boolean = false

    /** Groovy-specific method to change verbose. */
    fun verbose(verbose: Boolean) {
        this.verbose = verbose
    }

    /**
     * Open [outputDirectory] upon packing completion.
     * This is an optional property.
     */
    var openOnDone: Boolean = false

    /** Groovy-specific method to change open on done. */
    fun openOnDone(openOnDone: Boolean) {
        this.openOnDone = openOnDone
    }

    /** Configure macOS distribution. Unlike other distributions, mac configuration have some OS-specific properties. */
    @JvmOverloads
    fun macOS(init: ((@DistributionDsl MacOSDistribution).() -> Unit)? = null) {
        distributions[PackrConfig.Platform.MacOS] = MacOSDistribution().also { init?.invoke(it) }
    }

    /** Configure Windows 32-bit distribution. */
    @JvmOverloads
    fun windows32(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions[PackrConfig.Platform.Windows32] = SimpleDistribution().also { init?.invoke(it) }
    }

    /** Configure Windows 64-bit distribution. */
    @JvmOverloads
    fun windows64(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions[PackrConfig.Platform.Windows64] = SimpleDistribution().also { init?.invoke(it) }
    }

    /** Configure Linux 32-bit distribution. */
    @JvmOverloads
    fun linux32(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions[PackrConfig.Platform.Linux32] = SimpleDistribution().also { init?.invoke(it) }
    }

    /** Configure Linux 64-bit distribution. */
    @JvmOverloads
    fun linux64(init: ((@DistributionDsl Distribution).() -> Unit)? = null) {
        distributions[PackrConfig.Platform.Linux64] = SimpleDistribution().also { init?.invoke(it) }
    }
}