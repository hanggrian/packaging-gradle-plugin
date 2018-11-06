package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.hendraanggrian.packr.dist.MacOSDistribution
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.LogLevel.INFO
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.awt.Desktop
import java.io.IOException

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask() {

    @Input lateinit var extension: PackrExtension
    @Input lateinit var platform: PackrConfig.Platform

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        check(extension.mainClass.isNotEmpty()) { "Undefined main class" }

        val dist = extension.distributions.singleOrNull { it.platform == platform }
        if (dist == null) {
            logger.log(INFO, "No configuration found for $platform")
            return
        }
        logger.log(INFO, "Creating configuration for $platform")

        val config = PackrConfig()
        config.platform = dist.platform
        config.jdk = checkNotNull(dist.jdk) { "JDK path has not yet been specified" }
        config.executable = extension.executable
        config.classpath = extension.classpath
        config.mainClass = extension.mainClass
        config.outDir = extension.outputDir.resolve(dist.name ?: project.name)
        config.vmArgs = extension.vmArgs + dist.vmArgs
        config.resources = extension.resources
        config.minimizeJre = extension.minimizeJre
        if (dist is MacOSDistribution) {
            if (dist.icon != null) config.iconResource = dist.icon
            if (dist.bundleId != null) config.bundleIdentifier = dist.bundleId
        }
        config.verbose = extension.verbose

        when {
            config.outDir.exists() -> {
                logger.log(LogLevel.INFO, "Deleting old output")
                config.outDir.deleteRecursively()
            }
            else -> {
                logger.log(LogLevel.INFO, "Preparing output")
                extension.outputDir.mkdirs()
            }
        }
        Packr().pack(config)
        logger.log(LogLevel.INFO, "Pack completed")

        if (extension.openOnDone) {
            require(Desktop.isDesktopSupported()) { "Desktop is not supported, disable `openOnDone`" }
            Desktop.getDesktop().run {
                require(isSupported(Desktop.Action.OPEN)) { "Opening folder is not supported, disable `openOnDone`" }
                open(extension.outputDir)
            }
        }
    }
}