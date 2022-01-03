package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.LinuxOptions
import com.hendraanggrian.packaging.MacOptions
import com.hendraanggrian.packaging.PackagingExtension
import com.hendraanggrian.packaging.WindowsOptions
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

    override val windowsOptions: WindowsOptions = object : WindowsOptions {
        //region Platform dependent option for creating the application launcher
        override val console: Property<Boolean> = project.objects.property()
        //endregion

        //region Platform dependent options for creating the application package
        override val directoryChooser: Property<Boolean> = project.objects.property()

        override val menu: Property<Boolean> = project.objects.property()

        override val menuGroup: Property<String> = project.objects.property()

        override val perUserInstall: Property<Boolean> = project.objects.property()

        override val shortcut: Property<Boolean> = project.objects.property()

        override val upgradeUUID: Property<String> = project.objects.property()
        //endregion
    }

    override val macOptions: MacOptions = object : MacOptions {
        //region Platform dependent option for creating the application launcher
        override val packageIdentifier: Property<String> = project.objects.property()

        override val packageName: Property<String> = project.objects.property()

        override val bundleSigningPrefix: Property<String> = project.objects.property()

        override val sign: Property<Boolean> = project.objects.property()

        override val signingKeychain: RegularFileProperty = project.objects.fileProperty()

        override val signingKeyUserName: Property<String> = project.objects.property()
        //endregion
    }

    override val linuxOptions: LinuxOptions = object : LinuxOptions {
        //region Platform dependent options for creating the application package
        override val packageName: Property<String> = project.objects.property()

        override val debMaintainer: Property<String> = project.objects.property()

        override val menuGroup: Property<String> = project.objects.property()

        override val packageDependencies: Property<String> = project.objects.property()

        override val rpmLicenseType: Property<String> = project.objects.property()

        override val appRelease: Property<String> = project.objects.property()

        override val appCategory: Property<String> = project.objects.property()

        override val shortcut: Property<Boolean> = project.objects.property()
        //endregion
    }
}
