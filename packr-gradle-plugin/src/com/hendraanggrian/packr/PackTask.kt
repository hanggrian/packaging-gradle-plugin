package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.hendraanggrian.packr.dist.Distribution
import com.hendraanggrian.packr.dist.MacDistribution
import com.hendraanggrian.packr.internal.VMArged
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.awt.Desktop.Action.OPEN
import java.awt.Desktop.getDesktop
import java.io.File
import java.io.IOException

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask(), VMArged {

    companion object {
        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    private val packr: Packr = Packr()
    private val distributions: MutableList<Distribution> = mutableListOf()

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    @Input var executable: String? = null

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
    @InputFiles var resources: MutableList<String> = mutableListOf()

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

    @Input override var vmArgs: MutableList<String> = mutableListOf()

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
    @JvmOverloads fun mac(jdk: String, config: (MacDistribution.() -> Unit)? = null) {
        distributions += MacDistribution(project, jdk).apply { if (config != null) config() }
    }

    /** Configure Windows 32-bit distribution. */
    @JvmOverloads fun windows32(jdk: String, config: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(Platform.Windows32, project, jdk).apply { if (config != null) config() }
    }

    /** Configure Windows 64-bit distribution. */
    @JvmOverloads fun windows64(jdk: String, config: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(Platform.Windows64, project, jdk).apply { if (config != null) config() }
    }

    /** Configure Linux 32-bit distribution. */
    @JvmOverloads fun linux32(jdk: String, config: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(Platform.Linux32, project, jdk).apply { if (config != null) config() }
    }

    /** Configure Linux 64-bit distribution. */
    @JvmOverloads fun linux64(jdk: String, config: (Distribution.() -> Unit)? = null) {
        distributions += Distribution(Platform.Linux64, project, jdk).apply { if (config != null) config() }
    }

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        check(mainClass.isNotEmpty()) { "Undefined main class" }
        distributions.forEach {
            println("Packing ${it.platform}:")

            val config = PackrConfig()
            config.platform = it.platform
            config.jdk = checkNotNull(it.jdk) { "JDK path has not yet been initialized" }
            config.executable = executable!!
            config.classpath = classpath.map { project.projectDir.resolve(it).path }
            config.mainClass = mainClass
            config.outDir = project.projectDir.resolve(outputDir!!).resolve(it.name)
            config.vmArgs = vmArgs + it.vmArgs
            config.resources = resources.map { project.projectDir.resolve(it) }
            config.minimizeJre = minimization
            if (it is MacDistribution) {
                if (it.icon != null) config.iconResource = project.projectDir.resolve(it.icon!!)
                if (it.bundleId != null) config.bundleIdentifier = it.bundleId
            }
            config.verbose = verbose

            config.outDir.deleteRecursively()
            packr.pack(config)
        }

        if (openOnDone) getDesktop().run {
            require(isSupported(OPEN)) { "`openOnDone` is not supported in this system" }
            open(File(outputDir))
        }
    }
}