package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.LinuxPackaging
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

class DefaultLinuxPackaging(project: Project, defaultPackagingExtension: DefaultPackagingExtension) :
    DefaultPlatformPackaging(project, defaultPackagingExtension), LinuxPackaging {

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
