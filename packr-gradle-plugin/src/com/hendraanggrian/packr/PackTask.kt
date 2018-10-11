package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.dist.Distribution
import com.hendraanggrian.packr.dist.MacOSDistribution
import groovy.lang.Closure
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel.INFO
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.invoke
import java.awt.Desktop
import java.io.File
import java.io.IOException

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask(), VMArged {

    companion object {
        const val MINIMIZE_SOFT = "soft"
        const val MINIMIZE_HARD = "hard"
        const val MINIMIZE_ORACLEJRE8 = "oraclejre8"
    }

    private val packr = Packr()
    private val distributions = mutableListOf<Distribution>()

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    @Input var executable: String = ""

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    @Classpath @InputFiles var classpath: MutableList<String> = mutableListOf()

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Must be defined or will throw an exception.
     */
    @Input var mainClass: String = ""

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    @InputFiles var resources: MutableList<File> = mutableListOf()

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is `soft`.
     */
    @Input var minimizeJre: String = MINIMIZE_SOFT

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    @OutputDirectory lateinit var outputDir: File

    @Input override var vmArgs: MutableCollection<String> = mutableListOf()

    /**
     * Print extra messages about JRE minimizeJre when set to `true`.
     * This is an optional property.
     */
    @Input var verbose: Boolean = false

    /**
     * Open [outputDir] upon packing completion.
     * This is an optional property.
     */
    @Input var openOnDone: Boolean = false

    /** Configure macOS distribution. Unlike other distributions, mac configuration have some OS-specific properties. */
    @JvmOverloads fun macOS(init: Closure<MacOSDistribution>? = null) {
        distributions += MacOSDistribution(project.name).also { init?.invoke(it) }
    }

    /** Configure Windows 32-bit distribution. */
    @JvmOverloads fun windows32(init: Closure<Distribution>? = null) {
        distributions += Distribution(Platform.Windows32, project.name).also { init?.invoke(it) }
    }

    /** Configure Windows 64-bit distribution. */
    @JvmOverloads fun windows64(init: Closure<Distribution>? = null) {
        distributions += Distribution(Platform.Windows64, project.name).also { init?.invoke(it) }
    }

    /** Configure Linux 32-bit distribution. */
    @JvmOverloads fun linux32(init: Closure<Distribution>? = null) {
        distributions += Distribution(Platform.Linux32, project.name).also { init?.invoke(it) }
    }

    /** Configure Linux 64-bit distribution. */
    @JvmOverloads fun linux64(init: Closure<Distribution>? = null) {
        distributions += Distribution(Platform.Linux64, project.name).also { init?.invoke(it) }
    }

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        check(mainClass.isNotEmpty()) { "Undefined main class." }

        distributions.forEach { it ->
            logger.log(INFO, "Creating configuration for ${it.platform}.")

            val config = PackrConfig()
            config.platform = it.platform
            config.jdk = checkNotNull(it.jdk) { "JDK path has not yet been specified." }
            config.executable = executable
            config.classpath = classpath
            config.mainClass = mainClass
            config.outDir = outputDir.resolve(it.name)
            config.vmArgs = vmArgs + it.vmArgs
            config.resources = resources
            config.minimizeJre = minimizeJre
            if (it is MacOSDistribution) {
                if (it.icon != null) config.iconResource = it.icon
                if (it.bundleId != null) config.bundleIdentifier = it.bundleId
            }
            config.verbose = verbose

            when {
                config.outDir.exists() -> {
                    logger.log(INFO, "Deleting old output.")
                    config.outDir.deleteRecursively()
                }
                else -> {
                    logger.log(INFO, "Preparing output.")
                    outputDir.mkdirs()
                }
            }
            packr.pack(config)
            logger.log(INFO, "Pack completed.")
        }

        if (openOnDone) {
            require(Desktop.isDesktopSupported()) { "Desktop is not supported, disable `openOnDone`." }
            Desktop.getDesktop().run {
                require(isSupported(Desktop.Action.OPEN)) { "Opening folder is not supported, disable `openOnDone`." }
                open(outputDir)
            }
        }
    }
}