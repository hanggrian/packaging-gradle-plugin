package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.MacPackaging
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

class DefaultMacPackaging(project: Project, defaultPackagingExtension: DefaultPackagingExtension) :
    DefaultPlatformPackaging(project, defaultPackagingExtension), MacPackaging {

    //region Platform dependent option for creating the application launcher
    override val packageIdentifier: Property<String> = project.objects.property()

    override val packageName: Property<String> = project.objects.property()

    override val bundleSigningPrefix: Property<String> = project.objects.property()

    override val sign: Property<Boolean> = project.objects.property()

    override val signingKeychain: RegularFileProperty = project.objects.fileProperty()

    override val signingKeyUserName: Property<String> = project.objects.property()
    //endregion
}
