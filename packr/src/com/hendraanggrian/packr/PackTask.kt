package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
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
open class PackTask : DefaultTask(), PackrConfiguration {

    private val packr: Packr = Packr()
    private lateinit var platform: Platform

    @Input override var jdk: String? = null
    @Input override var executable: String? = null
    @Classpath @InputFiles override var classpath: MutableList<String> = mutableListOf()
    @Input override var mainClass: String? = null
    @Input override var vmArgs: MutableList<String> = mutableListOf()
    @InputFiles override var resources: MutableList<File> = mutableListOf()
    @Input override var minimizeJre: String? = null
    @Input override var outputName: String? = null
    @InputDirectory override var outputDir: File? = null

    @Optional @InputFile override var iconDir: File? = null
    @Optional @Input override var bundleId: String? = null
    @Optional @Input override var verbose: Boolean? = null
    @Optional @Input override var openOnDone: Boolean? = null

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        require(mainClass!!.isNotEmpty()) { "Undefined main class" }

        val outputFile = outputDir!!.resolve(outputName!!)
        outputFile.deleteRecursively()

        val config = PackrConfig()
        config.platform = platform
        config.jdk = jdk!!
        config.executable = executable!!
        config.classpath = classpath
        config.mainClass = mainClass!!
        config.outDir = outputFile
        config.vmArgs = vmArgs
        config.resources = resources
        config.minimizeJre = minimizeJre!!
        if (iconDir != null) config.iconResource = iconDir
        if (bundleId != null) config.bundleIdentifier = bundleId
        config.verbose = verbose!!

        packr.pack(config)

        if (openOnDone!!) getDesktop().run {
            require(isSupported(OPEN)) { "`openOnDone` is not supported in this system" }
            open(outputDir)
        }
    }

    internal fun platform(platform: Platform) {
        this.platform = platform
    }
}