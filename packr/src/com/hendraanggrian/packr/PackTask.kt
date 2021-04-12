@file:Suppress("UnstableApiUsage")

package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import java.awt.Desktop
import java.io.File

/** Task that will generate native distribution on each platform. */
open class PackTask : DefaultTask(), PackrGlobalConfiguration, PackrPlatformConfiguration {

    @get:Internal
    internal val platform: Property<PackrConfig.Platform> = project.objects.property()

    /**
     * File name of this distribution that will be generated.
     * Default is project's name.
     */
    @Input
    override val releaseName: Property<String> = project.objects.property()

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build
     * containing a JRE used to build this distribution.
     * Default is Java Home environment variable, if any.
     */
    @Input
    override val jdk: Property<String> = project.objects.property()

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property that is relevant only on macOS.
     */
    @Optional
    @Input
    override val icon: Property<File> = project.objects.property()

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property that is relevant only on macOS.
     */
    @Optional
    @Input
    override val bundleId: Property<String> = project.objects.property()

    @Input
    override val executable: Property<String> = project.objects.property()

    @InputFiles
    override val classpath: ListProperty<File> = project.objects.listProperty()

    @InputFiles
    override val removePlatformLibraries: ListProperty<File> = project.objects.listProperty()

    @Optional
    @Input
    override val mainClass: Property<String> = project.objects.property()

    @Input
    override val vmArgs: ListProperty<String> = project.objects.listProperty<String>()
        .convention(mutableListOf())

    @InputFiles
    override val resources: ListProperty<File> = project.objects.listProperty()

    @Input
    override val minimizeJre: Property<String> = project.objects.property()

    @OutputDirectory
    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Optional
    @OutputDirectory
    override val cacheJreDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Input
    override val verbose: Property<Boolean> = project.objects.property()

    @Input
    override val autoOpen: Property<Boolean> = project.objects.property()

    init {
        outputs.upToDateWhen { false } // always consider this task out of date
    }

    @TaskAction
    fun pack() {
        logger.info("Packing for ${platform.get().name}:")

        require(executable.get().isNotBlank()) { "Executable cannot be empty." }
        require(mainClass.isPresent) { "Undefined main class." }

        val config = PackrConfig()
        config.platform = platform.get()
        jdk.get().let {
            config.jdk = it
            logger.debug("jdk = $it")
        }
        executable.get().let {
            config.executable = it
            logger.debug("executable = $it")
        }
        classpath.get().flatMapJar().let {
            config.classpath = it
            logger.debug("classpath = ${it.joinToString()}")
        }
        removePlatformLibraries.get().flatMapJar().let {
            config.removePlatformLibs = it
            logger.debug("removePlatformLibs = ${it.joinToString()}")
        }
        mainClass.get().let {
            config.mainClass = it
            logger.debug("mainClass = $it")
        }
        vmArgs.get().let {
            config.vmArgs = it
            logger.debug("vmArgs = $it")
        }
        resources.get().let {
            config.resources = it
            logger.debug("resources = ${it.joinToString { file -> file.name }}")
        }
        minimizeJre.get().let {
            config.minimizeJre = it
            logger.debug("minimizeJre = $it")
        }
        outputDirectory.dir(releaseName).get().asFile.let {
            config.outDir = it
            logger.debug("outDir = $it")
        }
        cacheJreDirectory.orNull?.asFile.let {
            config.cacheJre = it
            logger.debug("cacheJre = $it")
        }
        verbose.get().let {
            config.verbose = it
            logger.debug("verbose = $it")
        }
        icon.orNull?.let {
            config.iconResource = it
            logger.debug("iconResource = $it")
        }
        bundleId.orNull?.let {
            config.bundleIdentifier = it
            logger.debug("bundleIdentifier = $it")
        }

        if (config.outDir.exists()) {
            logger.info("  Existing distribution '${config.outDir}' deleted")
            config.outDir.deleteRecursively()
        }
        config.outDir.mkdirs()

        Packr().pack(config)
        logger.info("  Build finished")

        if (autoOpen.get()) {
            when {
                !Desktop.isDesktopSupported() -> logger.info("  Desktop is not supported, ignoring auto open")
                else -> Desktop.getDesktop().run {
                    when {
                        !isSupported(Desktop.Action.OPEN) ->
                            logger.info("  Opening folder is not supported, ignoring auto open")
                        else -> {
                            logger.info("  Auto opening directory")
                            open(config.outDir)
                        }
                    }
                }
            }
        }
    }

    private fun Iterable<File>.flatMapJar() = flatMap<File, String> { file ->
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
