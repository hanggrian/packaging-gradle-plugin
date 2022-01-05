package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.Packaging
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import java.io.File

abstract class DefaultPlatformPackaging(project: Project, defaultPackagingExtension: DefaultPackagingExtension) :
    Packaging {

    //region Generic Options
    override val appVersion: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.appVersion)

    override val copyright: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.copyright)

    override val appDescription: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.appDescription)

    override val appName: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.appName)

    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(defaultPackagingExtension.outputDirectory)

    override val vendor: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.vendor)

    override val verbose: Property<Boolean> = project.objects.property<Boolean>()
        .convention(defaultPackagingExtension.verbose)
    //endregion

    //region Options for creating the runtime image
    override val addModules: ListProperty<String> = project.objects.listProperty<String>()
        .convention(defaultPackagingExtension.addModules)

    override val modulePath: ListProperty<File> = project.objects.listProperty<File>()
        .convention(defaultPackagingExtension.modulePath)

    override val bindServices: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.bindServices)

    override val runtimeImage: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.runtimeImage)
    //endregion

    //region Options for creating the application image
    override val icon: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.icon)

    override val inputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(defaultPackagingExtension.inputDirectory)
    //endregion

    //region Options for creating the application launcher(s)
    override val addLauncher: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.addLauncher)

    override val arguments: ListProperty<String> = project.objects.listProperty<String>()
        .convention(defaultPackagingExtension.arguments)

    override val javaOptions: ListProperty<String> = project.objects.listProperty<String>()
        .convention(defaultPackagingExtension.javaOptions)

    override val mainClass: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.mainClass)

    override val mainJar: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.mainJar)

    override val module: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.module)
    //endregion

    //region Options for creating the application package
    override val appImage: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.appImage)

    override val fileAssociations: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.fileAssociations)

    override val installDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(defaultPackagingExtension.installDirectory)

    override val licenseFile: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.licenseFile)

    override val resourcesDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(defaultPackagingExtension.resourcesDirectory)
    //endregion
}
