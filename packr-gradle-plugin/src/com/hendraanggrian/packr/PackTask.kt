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

/**
 * Task that will generate native distribution on each platform.
 * Logging is completely avoided here to since [Packr] is already printing information even if logger is deactivated.
 */
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
        val config = PackrConfig()
        config.platform = distribution.platform
        config.jdk = checkNotNull(distribution.jdk) { "Undefined JDK path" }
        config.executable = executable
        config.classpath = classpath.flatMapJar()
        config.removePlatformLibs = removePlatformLibs.flatMapJar()
        config.mainClass = checkNotNull(mainClass) { "Undefined main class" }
        config.vmArgs = vmArgs + distribution.vmArgs
        config.resources = resources.toList()
        config.minimizeJre = minimizeJre
        config.outDir = outputDir.resolve(distribution.name)
        cacheJreDir?.let { config.cacheJre = it }
        config.verbose = isVerbose

        (distribution as? MacOSDistribution)?.run {
            icon?.let { config.iconResource = it }
            bundleId?.let { config.bundleIdentifier = it }
        }

        if (config.outDir.exists()) {
            config.outDir.deleteRecursively()
        }
        outputDir.mkdirs()

        Packr().pack(config)

        if (isAutoOpen) {
            when {
                !Desktop.isDesktopSupported() -> logger.info("Desktop is not supported, ignoring auto open.")
                else -> Desktop.getDesktop().run {
                    when {
                        !isSupported(Desktop.Action.OPEN) ->
                            logger.info("Opening folder is not supported, ignoring auto open.")
                        else -> open(outputDir)
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
