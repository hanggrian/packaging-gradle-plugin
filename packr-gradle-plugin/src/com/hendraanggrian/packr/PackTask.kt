package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.awt.Desktop
import java.io.File

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask(), PackrConfiguration {

    @Input lateinit var distribution: Distribution

    override val projectDir: File @Internal get() = project.projectDir

    @Input override lateinit var executable: String
    @Input override lateinit var classpath: Iterable<File>
    @Input override lateinit var removePlatformLibs: Iterable<File>
    @Optional @Input override var mainClass: String? = null
    @Input override lateinit var vmArgs: Iterable<String>
    @Input override lateinit var resources: Iterable<File>
    @Input override lateinit var minimizeJre: String

    @OutputDirectory override lateinit var outputDir: File
    override var outputDirectory: String
        @Input get() = super.outputDirectory
        set(value) {
            super.outputDirectory = value
        }

    @Optional @OutputDirectory override var cacheJreDir: File? = null
    override var cacheJreDirectory: String?
        @Optional @Input get() = super.cacheJreDirectory
        set(value) {
            super.cacheJreDirectory = value
        }

    override var isVerbose: Boolean = false @Input get
    override var isAutoOpen: Boolean = false @Input get

    init {
        outputs.upToDateWhen { false } // always consider this task out of date
    }

    @TaskAction fun pack() {
        logger.info("Packing for $distribution:")

        require(executable.isNotBlank()) { "Executable cannot be empty." }
        checkNotNull(distribution.jdk) { "Undefined JDK." }
        checkNotNull(mainClass) { "Undefined main class." }

        val config = PackrConfig()
        config.platform = distribution.platform
        config.jdk = distribution.jdk!!
        logger.debug("jdk = ${distribution.jdk}")
        config.executable = executable
        logger.debug("executable = $executable")
        config.classpath = classpath.flatMapJar()
        logger.debug("classpath = $classpath")
        config.removePlatformLibs = removePlatformLibs.flatMapJar()
        logger.debug("removePlatformLibs = $removePlatformLibs")
        config.mainClass = mainClass!!
        logger.debug("mainClass = $mainClass")
        config.vmArgs = vmArgs + distribution.vmArgs
        logger.debug("vmArgs = ${config.vmArgs}")
        config.resources = resources.toList()
        logger.debug("resources = $resources")
        config.minimizeJre = minimizeJre
        logger.debug("minimizeJre = $minimizeJre")
        config.outDir = outputDir.resolve(distribution.name)
        logger.debug("outDir = ${config.outDir}")
        if (cacheJreDir != null) config.cacheJre = cacheJreDir
        logger.debug("cacheJre = $cacheJreDir")
        config.verbose = isVerbose
        logger.debug("verbose = $isVerbose")

        (distribution as? MacOSDistribution)?.run {
            if (icon != null) config.iconResource = icon
            logger.debug("iconResource = $icon")
            if (bundleId != null) config.bundleIdentifier = bundleId
            logger.debug("bundleIdentifier = $bundleId")
        }

        if (config.outDir.exists()) {
            logger.info("  Existing distribution '${config.outDir}' deleted")
            config.outDir.deleteRecursively()
        }
        outputDir.mkdirs()

        Packr().pack(config)
        logger.info("  Build finished")

        if (isAutoOpen) {
            when {
                !Desktop.isDesktopSupported() -> logger.info("  Desktop is not supported, ignoring auto open")
                else -> Desktop.getDesktop().run {
                    when {
                        !isSupported(Desktop.Action.OPEN) ->
                            logger.info("  Opening folder is not supported, ignoring auto open")
                        else -> {
                            logger.info("  Auto opening directory")
                            open(outputDir)
                        }
                    }
                }
            }
        }
    }

    private fun Iterable<File>.flatMapJar() = flatMap { file ->
        when {
            file.isDirectory ->
                checkNotNull(file.listFiles()) { "Unable to list files in directory: ${file.absolutePath}" }
                    .filter { it.isJar() }
                    .map { it.absolutePath }
            file.isJar() -> listOf(file.absolutePath)
            else -> emptyList()
        }
    }

    private fun File.isJar(): Boolean = isFile && extension == "jar"
}
