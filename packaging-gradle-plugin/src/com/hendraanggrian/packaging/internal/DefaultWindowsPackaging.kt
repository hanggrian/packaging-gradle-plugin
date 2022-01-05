package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.WindowsPackaging
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

class DefaultWindowsPackaging(project: Project, defaultPackagingExtension: DefaultPackagingExtension) :
    DefaultPlatformPackaging(project, defaultPackagingExtension), WindowsPackaging {

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
