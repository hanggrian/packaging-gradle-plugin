package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.PackSpec
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import java.io.File

/** Task that will generate native distribution on each platform. */
abstract class AbstractPackTask : Exec(), PackSpec {

    @get:Internal
    internal val verbose: Property<Boolean> = project.objects.property()

    //region Generic Options
    @Input
    override val appVersion: Property<String> = project.objects.property()

    @Optional
    @Input
    override val copyright: Property<String> = project.objects.property()

    @Optional
    @Input
    override val appDescription: Property<String> = project.objects.property()

    @Input
    override val appName: Property<String> = project.objects.property()

    @OutputDirectory
    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Optional
    @Input
    override val vendor: Property<String> = project.objects.property()
    //endregion

    //region Options for creating the runtime image
    @Input
    override val addModules: ListProperty<String> = project.objects.listProperty()

    @InputFiles
    override val modulePath: ListProperty<File> = project.objects.listProperty()

    @Optional
    @Input
    override val bindServices: Property<String> = project.objects.property()

    @Optional
    @InputFile
    override val runtimeImage: RegularFileProperty = project.objects.fileProperty()
    //endregion

    //region Options for creating the application image
    @Optional
    @InputFile
    override val icon: RegularFileProperty = project.objects.fileProperty()

    @Classpath
    override val inputDirectory: DirectoryProperty = project.objects.directoryProperty()
    //endregion

    //region Options for creating the application launcher(s)
    @Optional
    @InputFile
    override val addLauncher: RegularFileProperty = project.objects.fileProperty()

    @Input
    override val arguments: ListProperty<String> = project.objects.listProperty()

    @Input
    override val javaOptions: ListProperty<String> = project.objects.listProperty()

    @Input
    override val mainClass: Property<String> = project.objects.property()

    @Input
    override val mainJar: Property<String> = project.objects.property()

    @Optional
    @Input
    override val module: Property<String> = project.objects.property()
    //endregion

    //region Options for creating the application package
    @Optional
    @InputFile
    override val appImage: RegularFileProperty = project.objects.fileProperty()

    @Optional
    @InputFile
    override val fileAssociations: RegularFileProperty = project.objects.fileProperty()

    @Optional
    @InputDirectory
    override val installDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Optional
    @InputFile
    override val licenseFile: RegularFileProperty = project.objects.fileProperty()

    @Optional
    @InputDirectory
    override val resourcesDirectory: DirectoryProperty = project.objects.directoryProperty()
    //endregion

    init {
        outputs.upToDateWhen { false } // always consider this task out of date
    }

    /** Convenient method to add items to [addModules]. */
    fun addModules(vararg modules: String) {
        addModules.addAll(*modules)
    }

    /** Convenient method to add items to [modulePath]. */
    fun modulePath(vararg paths: File) {
        modulePath.addAll(*paths)
    }

    /** Convenient method to add items to [arguments]. */
    fun arguments(vararg args: String) {
        arguments.addAll(*args)
    }

    /** Convenient method to add items to [javaOptions]. */
    fun javaOptions(vararg options: String) {
        javaOptions.addAll(*options)
    }

    override fun exec() {
        val lines = mutableListOf(
            "jpackage",
            "--app-version", appVersion.get(),
            "--name", appName.get(),
            "--dest", outputDirectory.asFile.get().absolutePath,
            "--input", inputDirectory.asFile.get().absolutePath,
            "--main-class", mainClass.get(),
            "--main-jar", mainJar.get(),
        )
        if (verbose.get()) {
            lines += "--verbose"
        }
        copyright.orNull?.let {
            lines += "--copyright"
            lines += it
        }
        appDescription.orNull?.let {
            lines += "--description"
            lines += it
        }
        vendor.orNull?.let {
            lines += "--vendor"
            lines += it
        }
        addModules.orNull?.forEach {
            lines += "--add-modules"
            lines += it
        }
        modulePath.orNull?.forEach {
            lines += "--module-path"
            lines += it.absolutePath
        }
        bindServices.orNull?.let {
            lines += "--bind-services"
            lines += it
        }
        runtimeImage.orNull?.let {
            lines += "--runtime-image"
            lines += it.asFile.absolutePath
        }
        icon.orNull?.let {
            lines += "--icon"
            lines += it.asFile.absolutePath
        }
        addLauncher.orNull?.let {
            lines += "--add-launcher"
            lines += it.asFile.absolutePath
        }
        arguments.orNull?.forEach {
            lines += "--arguments"
            lines += "'$it'"
        }
        javaOptions.orNull?.forEach {
            lines += "--java-options"
            lines += it
        }
        module.orNull?.let {
            lines += "--module"
            lines += it
        }
        appImage.orNull?.let {
            lines += "--app-image"
            lines += it.asFile.absolutePath
        }
        fileAssociations.orNull?.let {
            lines += "--file-associations"
            lines += it.asFile.absolutePath
        }
        installDirectory.orNull?.let {
            lines += "--install-dir"
            lines += it.asFile.absolutePath
        }
        licenseFile.orNull?.let {
            lines += "--license-file"
            lines += it.asFile.absolutePath
        }
        resourcesDirectory.orNull?.let {
            lines += "--resource-dir"
            lines += it.asFile.absolutePath
        }
        commandLine(lines + platformDependentLines)
        super.exec()
    }

    @get:Internal
    abstract val platformDependentLines: List<String>
}
