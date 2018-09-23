package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.dist.Distribution
import com.hendraanggrian.packr.dist.MacDistribution
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.awt.Desktop
import java.io.File
import java.io.IOException

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask(), VMArged {

    companion object {
        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    private val packr = Packr()
    private val distributions: MutableCollection<Distribution> = mutableListOf()

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    @Input var executable: String? = null

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    @Classpath @InputFiles var classpath: MutableCollection<String> = mutableListOf()

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Must be defined or will throw an exception.
     */
    @Input var mainClass: String = ""

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    @InputFiles var resources: MutableCollection<String> = mutableListOf()

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is `soft`.
     */
    @Input var minimization: String = MINIMIZATION_SOFT

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    @InputDirectory var outputDir: String? = null

    @Input override var vmArgs: MutableCollection<String> = mutableListOf()

    /**
     * Print extra messages about JRE minimization when set to `true`.
     * This is an optional property.
     */
    @Input var verbose: Boolean = false

    /**
     * Open [outputDir] upon packing completion.
     * This is an optional property.
     */
    @Input var openOnDone: Boolean = false

    /** Configure macOS distribution. Unlike other distributions, mac configuration have some OS-specific properties. */
    @JvmOverloads fun mac(init: (MacDistribution.() -> Unit)? = null) {
        distributions += MacDistribution(project).apply { if (init != null) init() }
    }

    /** Configure Windows 32-bit distribution. */
    @JvmOverloads fun windows32(init: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(project, Platform.Windows32).apply { if (init != null) init() }
    }

    /** Configure Windows 64-bit distribution. */
    @JvmOverloads fun windows64(init: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(project, Platform.Windows64).apply { if (init != null) init() }
    }

    /** Configure Linux 32-bit distribution. */
    @JvmOverloads fun linux32(init: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(project, Platform.Linux32).apply { if (init != null) init() }
    }

    /** Configure Linux 64-bit distribution. */
    @JvmOverloads fun linux64(init: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(project, Platform.Linux64).apply { if (init != null) init() }
    }

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        check(mainClass.isNotEmpty()) { "Undefined main class" }
        distributions.forEach { dist ->
            println("Packing ${dist.platform}:")

            val config = PackrConfig()
            config.platform = dist.platform
            config.jdk = checkNotNull(dist.jdk) { "JDK path has not yet been initialized" }
            config.executable = executable!!
            config.classpath = classpath.map { project.projectDir.resolve(it).path }
            config.mainClass = mainClass
            config.outDir = project.projectDir.resolve(outputDir!!).resolve(dist.name)
            config.vmArgs = vmArgs + dist.vmArgs
            config.resources = resources.map { project.projectDir.resolve(it) }
            config.minimizeJre = minimization
            if (dist is MacDistribution) {
                if (dist.icon != null) config.iconResource = project.projectDir.resolve(dist.icon!!)
                if (dist.bundleId != null) config.bundleIdentifier = dist.bundleId
            }
            config.verbose = verbose

            config.outDir.deleteRecursively()
            packr.pack(config)
        }

        if (openOnDone) {
            require(Desktop.isDesktopSupported()) { "Desktop is not supported, disable `openOnDone`." }
            Desktop.getDesktop().run {
                require(isSupported(Desktop.Action.OPEN)) { "Opening folder is not supported, disable `openOnDone`." }
                open(File(outputDir))
            }
        }
    }
}