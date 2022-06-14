package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.DefaultLinuxPackSpec
import com.hendraanggrian.packaging.DefaultMacPackSpec
import com.hendraanggrian.packaging.DefaultWindowsPackSpec
import com.hendraanggrian.packaging.LinuxPackSpec
import com.hendraanggrian.packaging.MacPackSpec
import com.hendraanggrian.packaging.PackagingExtension
import com.hendraanggrian.packaging.WindowsPackSpec
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
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
    override val modules: SetProperty<String> = project.objects.setProperty()

    override val modulePaths: SetProperty<File> = project.objects.setProperty()

    override val bindServices: Property<String> = project.objects.property()

    override val runtimeImage: RegularFileProperty = project.objects.fileProperty()
    //endregion

    //region Options for creating the application image
    override val icon: RegularFileProperty = project.objects.fileProperty()

    override val inputDirectory: DirectoryProperty = project.objects.directoryProperty()
    //endregion

    //region Options for creating the application launcher(s)
    override val launcher: RegularFileProperty = project.objects.fileProperty()

    override val args: ListProperty<String> = project.objects.listProperty()

    override val javaArgs: ListProperty<String> = project.objects.listProperty()

    override val mainClass: Property<String> = project.objects.property()

    override val mainJar: Property<String> = project.objects.property()

    override val mainModule: Property<String> = project.objects.property()
    //endregion

    //region Options for creating the application package
    override val appImage: RegularFileProperty = project.objects.fileProperty()

    override val fileAssociations: RegularFileProperty = project.objects.fileProperty()

    override val installDirectory: DirectoryProperty = project.objects.directoryProperty()

    override val license: RegularFileProperty = project.objects.fileProperty()

    override val resourcesDirectory: DirectoryProperty = project.objects.directoryProperty()
    //endregion

    override val windowsSpec: Property<WindowsPackSpec> = project.objects.property<WindowsPackSpec>()
        .value(DefaultWindowsPackSpec(project, @Suppress("LeakingThis") this))

    override val macSpec: Property<MacPackSpec> = project.objects.property<MacPackSpec>()
        .value(DefaultMacPackSpec(project, @Suppress("LeakingThis") this))

    override val linuxSpec: Property<LinuxPackSpec> = project.objects.property<LinuxPackSpec>()
        .value(DefaultLinuxPackSpec(project, @Suppress("LeakingThis") this))
}
