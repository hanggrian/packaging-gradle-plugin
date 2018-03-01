package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.badlogicgames.packr.PackrConfig.Platform.MacOS
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException

open class PackTask : DefaultTask() {

    private lateinit var platform: Platform

    @Input var jdk: String? = null
    @Input var executable: String = project.name
    @Input val classpath: MutableList<String> = mutableListOf()
    @Input var mainClass: String? = null
    @Input val vmArgs: MutableList<String> = mutableListOf()
    @Input val resources: MutableList<File> = mutableListOf()
    @Input var minimizeJRE: String = "soft"
    @Input var outputName: String = project.name
    @InputDirectory var outputDir: File = project.buildDir.resolve("release")
    @InputDirectory var iconDirectory: File = File("")
    @Input var bundleIdentifier: String = ""

    internal fun setPlatform(platform: Platform) {
        this.platform = platform
    }

    /**
     * File locations of the JAR files to package.
     * Input [jars] are relative to project directory.
     */
    fun classpath(vararg jars: String) {
        classpath += jars.map { project.projectDir.resolve(it).path }
    }

    /**
     * File locations of the JAR files to package.
     * Input [jars] are absolute.
     */
    fun classpath(vararg jars: File) {
        classpath += jars.map { it.path }
    }

    /**
     * List of files and directories to be packaged next to the native executable.
     * Input [files] are relative to project directory.
     */
    fun resources(vararg files: String) {
        resources += files.map { project.projectDir.resolve(it) }
    }

    /**
     * List of files and directories to be packaged next to the native executable.
     * Input [resources] are absolute.
     */
    fun resources(vararg files: File) {
        resources += files
    }

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        val outputFile = if (platform == MacOS) outputDir.resolve("$outputName.app") else outputDir.resolve(outputName)
        outputFile.deleteRecursively()

        val config = PackrConfig()
        config.platform = platform
        config.jdk = jdk
        config.executable = executable
        config.classpath = classpath
        config.mainClass = mainClass ?: error("Undefined main class")
        config.outDir = outputFile
        config.vmArgs = vmArgs
        config.resources = resources
        config.minimizeJre = minimizeJRE
        if (iconDirectory.path.isNotEmpty()) config.iconResource = iconDirectory
        if (bundleIdentifier.isNotEmpty()) config.bundleIdentifier = bundleIdentifier
        config.verbose = true // always print minimization result

        Packr().pack(config)
    }
}