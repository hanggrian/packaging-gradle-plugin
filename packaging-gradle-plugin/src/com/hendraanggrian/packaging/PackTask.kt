package com.hendraanggrian.packaging

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
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
open class PackTask : DefaultTask(), PackSpec {

    @get:Internal
    internal val platform: Property<PackrConfig.Platform> = project.objects.property()

    @get:Internal
    internal val verbose: Property<Boolean> = project.objects.property()

    @get:Internal
    internal val autoOpen: Property<Boolean> = project.objects.property()

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build
     * containing a JRE used to build this distribution.
     * Default is Java Home environment variable, if any.
     */
    @Input
    val jdk: Property<String> = project.objects.property()

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property that is relevant only on macOS.
     */
    @Optional
    @Input
    val icon: Property<File> = project.objects.property()

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property that is relevant only on macOS.
     */
    @Optional
    @Input
    val bundleId: Property<String> = project.objects.property()

    @Input
    override val appName: Property<String> = project.objects.property()

    @Input
    override val executable: Property<String> = project.objects.property()

    @InputDirectory
    override val classpath: DirectoryProperty = project.objects.directoryProperty()

    @InputFiles
    override val removePlatformLibraries: ListProperty<File> = project.objects.listProperty()

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

    init {
        outputs.upToDateWhen { false } // always consider this task out of date
    }

    @TaskAction
    fun pack() {
        logger.info("Packing for ${platform.get().name}:")
        val config = PackrConfig()
        config.platform = platform.get()
        config.jdk = jdk.get()
        logger.debug("jdk = ${config.jdk}")
        config.executable = executable.get()
        logger.debug("executable = ${config.executable}")
        config.classpath = classpath.get().asFile.listFiles()!!.asIterable().flatMapJar()
        logger.debug("classpath = ${config.classpath.joinToString()}")
        config.removePlatformLibs = removePlatformLibraries.get().flatMapJar()
        logger.debug("removePlatformLibs = ${config.removePlatformLibs.joinToString()}")
        config.mainClass = mainClass.get()
        logger.debug("mainClass = ${config.mainClass}")
        config.vmArgs = vmArgs.get()
        logger.debug("vmArgs = ${config.vmArgs}")
        config.resources = resources.get()
        logger.debug("resources = ${config.resources.joinToString { file -> file.name }}")
        config.minimizeJre = minimizeJre.get()
        logger.debug("minimizeJre = ${config.minimizeJre}")
        config.outDir = outputDirectory.dir("${platform.get()}/${appName.get()}").get().asFile
        logger.debug("outDir = ${config.outDir}")
        config.cacheJre = cacheJreDirectory.orNull?.asFile
        logger.debug("cacheJre = ${config.cacheJre}")
        config.verbose = verbose.get()
        logger.debug("verbose = ${config.verbose}")
        config.iconResource = icon.orNull
        logger.debug("iconResource = ${config.iconResource}")
        config.bundleIdentifier = bundleId.orNull
        logger.debug("bundleIdentifier = ${config.bundleIdentifier}")

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
