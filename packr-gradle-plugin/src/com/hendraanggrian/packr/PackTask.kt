package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import java.awt.Desktop
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask() {

    @Input lateinit var extension: PackrExtension
    @Input lateinit var platform: PackrConfig.Platform

    init {
        // always consider this task out of date
        outputs.upToDateWhen { false }
    }

    @TaskAction fun pack() {
        val distribution = extension[platform]
        if (distribution == null) {
            logger.info("No configuration found for $platform")
            return
        }
        logger.info("Creating configuration for $platform")

        val config = PackrConfig()
        config.platform = platform
        config.jdk = checkNotNull(distribution.jdk) { "Undefined JDK path" }
        config.executable = checkNotNull(extension.executable) { "Undefined executable" }
        config.classpath = extension.classpath.flatMapJar()
        config.removePlatformLibs = extension.removePlatformLibs.flatMapJar()
        config.mainClass = checkNotNull(extension.mainClass) { "Undefined main class" }
        config.vmArgs = extension.vmArgs + distribution.vmArgs
        config.resources = extension.resources.toList()
        config.minimizeJre = extension.minimizeJre
        config.outDir = extension.outputDir.resolve(distribution.name ?: project.name)
        extension.cacheJreDirectory?.let { config.cacheJre = File(it) }
        config.verbose = extension.isVerbose

        if (distribution is MacOSDistribution) {
            distribution.icon?.let { config.iconResource = it }
            distribution.bundleId?.let { config.bundleIdentifier = it }
        }

        if (config.outDir.exists()) {
            logger.info("Deleting old output")
            config.outDir.deleteRecursively()
        }

        logger.info("Preparing output")
        extension.outputDir.mkdirs()

        Packr().pack(config)
        logger.info("Pack completed")

        if (extension.isAutoOpen) {
            when {
                !Desktop.isDesktopSupported() -> logger.info("Desktop is not supported, ignoring `isAutoOpen`")
                else -> Desktop.getDesktop().run {
                    when {
                        !isSupported(Desktop.Action.OPEN) ->
                            logger.info("Opening folder is not supported, ignoring `isAutoOpen`")
                        else -> open(extension.outputDir)
                    }
                }
            }
        }
    }

    private fun Iterable<File>.flatMapJar() = flatMap { file ->
        when {
            file.isDirectory -> checkNotNull(file.listFiles()) { "Unable to list files in directory: ${file.absolutePath}" }
                .filter { it.isJar() }
                .map { it.absolutePath }
            file.isJar() -> listOf(file.absolutePath)
            else -> emptyList()
        }
    }

    private fun File.isJar(): Boolean = extension == "jar"
}
