package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform.MacOS
import com.badlogicgames.packr.PackrConfig.Platform.values
import com.badlogicgames.packr.desc
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.awt.Desktop.Action.OPEN
import java.awt.Desktop.getDesktop
import java.io.File
import java.io.IOException

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask() {

    private val packr: Packr = Packr()

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
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    @Input var vmArgs: MutableList<String> = mutableListOf()

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
    @Input var minimizeJre: String = "soft"

    /**
     * The output name.
     * Default is project's name.
     */
    @Input var outputName: String? = null

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    @InputDirectory var outputDir: File? = null

    /**
     * Append app file extension to mac distribution.
     * Default is true.
     */
    @Input var wrapApp: Boolean = true

    /**
     * Location of an AppBundle icon resource (.icns file).
     * This is an optional property.
     */
    @Optional @InputFile var iconDir: File? = null

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    @Optional @Input var bundleId: String? = null

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

    /** Add relative file locations of the JAR files to package. */
    fun classpath(vararg jars: String): Boolean = classpath.addAll(jars.map { project.projectDir.resolve(it).path })

    /** Add absolute file locations of the JAR files to package. */
    fun classpath(vararg jars: File): Boolean = classpath.addAll(jars.map { it.path })

    /** Add VM arguments. */
    fun vmArgs(vararg args: String): Boolean = vmArgs.addAll(args)

    /** Add relative resources files or directories. */
    fun resources(vararg files: String): Boolean = resources.addAll(files.map { project.projectDir.resolve(it) })

    /** Add absolute resources files or directories. */
    fun resources(vararg files: File): Boolean = resources.addAll(files)

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        val platforms = values().filter { project.hasProperty(it.desc) }
        require(platforms.isNotEmpty()) { "Missing platform" }
        require(mainClass.isNotEmpty()) { "Undefined main class" }

        platforms.forEach {
            println("Packing $it:")

            val config = PackrConfig()
            config.platform = it
            config.jdk = project.findProperty(it.desc).toString()
            config.executable = executable!!
            config.classpath = classpath
            config.mainClass = mainClass
            config.outDir = outputDir!!.resolve("$outputName-${it.desc}${if (it == MacOS && wrapApp) ".app" else ""}")
            config.vmArgs = vmArgs
            config.resources = resources
            config.minimizeJre = minimizeJre
            if (iconDir != null) config.iconResource = iconDir
            if (bundleId != null) config.bundleIdentifier = bundleId
            config.verbose = verbose

            config.outDir.deleteRecursively()
            packr.pack(config)
        }

        if (openOnDone) getDesktop().run {
            require(isSupported(OPEN)) { "`openOnDone` is not supported in this system" }
            open(outputDir)
        }
    }
}