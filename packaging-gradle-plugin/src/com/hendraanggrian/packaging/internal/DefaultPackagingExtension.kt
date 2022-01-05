package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.LinuxPackaging
import com.hendraanggrian.packaging.MacPackaging
import com.hendraanggrian.packaging.PackagingExtension
import com.hendraanggrian.packaging.WindowsPackaging
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import java.io.File

open class DefaultPackagingExtension(project: Project) : PackagingExtension {

    //region Generic Options
    override val appVersion: Property<String> = project.objects.property()

    override val copyright: Property<String> = project.objects.property()

    override val appDescription: Property<String> = project.objects.property()

    override val appName: Property<String> = project.objects.property()

    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("install"))

    override val vendor: Property<String> = project.objects.property()

    override val verbose: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)
    //endregion

    //region Options for creating the runtime image
    override val addModules: ListProperty<String> = project.objects.listProperty()

    override val modulePath: ListProperty<File> = project.objects.listProperty()

    override val bindServices: Property<String> = project.objects.property()

    override val runtimeImage: RegularFileProperty = project.objects.fileProperty()
    //endregion

    //region Options for creating the application image
    override val icon: RegularFileProperty = project.objects.fileProperty()

    override val inputDirectory: DirectoryProperty = project.objects.directoryProperty()
    //endregion

    //region Options for creating the application launcher(s)
    override val addLauncher: RegularFileProperty = project.objects.fileProperty()

    override val arguments: ListProperty<String> = project.objects.listProperty()

    override val javaOptions: ListProperty<String> = project.objects.listProperty()

    override val mainClass: Property<String> = project.objects.property()

    override val mainJar: Property<String> = project.objects.property()

    override val module: Property<String> = project.objects.property()
    //endregion

    //region Options for creating the application package
    override val appImage: RegularFileProperty = project.objects.fileProperty()

    override val fileAssociations: RegularFileProperty = project.objects.fileProperty()

    override val installDirectory: DirectoryProperty = project.objects.directoryProperty()

    override val licenseFile: RegularFileProperty = project.objects.fileProperty()

    override val resourcesDirectory: DirectoryProperty = project.objects.directoryProperty()
    //endregion

    @Suppress("LeakingThis")
    override val windowsOptions: WindowsPackaging = DefaultWindowsPackaging(project, this)

    @Suppress("LeakingThis")
    override val macOptions: MacPackaging = DefaultMacPackaging(project, this)

    @Suppress("LeakingThis")
    override val linuxOptions: LinuxPackaging = DefaultLinuxPackaging(project, this)
}
