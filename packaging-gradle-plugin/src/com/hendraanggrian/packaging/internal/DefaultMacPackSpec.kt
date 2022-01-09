package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.MacPackSpec
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

class DefaultMacPackSpec(project: Project, defaultPackagingExtension: DefaultPackaging) :
    DefaultPackSpec(project, defaultPackagingExtension), MacPackSpec {

    //region Platform dependent option for creating the application launcher
    override val packageIdentifier: Property<String> = project.objects.property()

    override val packageName: Property<String> = project.objects.property()

    override val bundleSigningPrefix: Property<String> = project.objects.property()

    override val sign: Property<Boolean> = project.objects.property()

    override val signingKeychain: RegularFileProperty = project.objects.fileProperty()

    override val signingKeyUserName: Property<String> = project.objects.property()
    //endregion
}
