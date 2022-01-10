package com.hendraanggrian.packaging

import com.hendraanggrian.packaging.internal.DefaultPackaging
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

/** Reverse the effect of [kr.motd.maven.os.Detector.normalizeArch]. */
internal fun denormalizeArch(value: String): String = when {
    value.startsWith("x86_64") -> "x64"
    value.startsWith("x86_32") -> "x32"
    value.startsWith("itanium_64") -> "itanium64"
    value.startsWith("itanium_32") -> "itanium32"
    value.startsWith("sparc_32") -> "sparc32"
    value.startsWith("sparc_64") -> "sparc64"
    value.startsWith("arm_32") -> "arm32"
    value.startsWith("aarch_64") -> "arm64"
    value.startsWith("mips_32") -> "mips32"
    value.startsWith("mipsel_32") -> "mips32el"
    value.startsWith("mips_64") -> "mips64"
    value.startsWith("mipsel_64") -> "mips64el"
    value.startsWith("ppc_32") -> "ppc32"
    value.startsWith("ppcle_32") -> "ppc32le"
    value.startsWith("ppc_64") -> "ppc64"
    value.startsWith("ppcle_64") -> "ppc64le"
    value.startsWith("s390_32") -> "s390"
    value.startsWith("s390_64") -> "s390x"
    else -> value
}

internal class DefaultLinuxPackSpec(project: Project, defaultPackagingExtension: DefaultPackaging) :
    DefaultPackSpec(project, defaultPackagingExtension), LinuxPackSpec {

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

internal class DefaultWindowsPackSpec(project: Project, defaultPackagingExtension: DefaultPackaging) :
    DefaultPackSpec(project, defaultPackagingExtension), WindowsPackSpec {

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

internal class DefaultMacPackSpec(project: Project, defaultPackagingExtension: DefaultPackaging) :
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

internal abstract class DefaultPackSpec(project: Project, defaultPackagingExtension: DefaultPackaging) : PackSpec {

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
    override val modules: SetProperty<String> = project.objects.setProperty<String>()
        .convention(defaultPackagingExtension.modules)

    override val modulePaths: SetProperty<File> = project.objects.setProperty<File>()
        .convention(defaultPackagingExtension.modulePaths)

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
    override val launcher: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.launcher)

    override val args: ListProperty<String> = project.objects.listProperty<String>()
        .convention(defaultPackagingExtension.args)

    override val javaArgs: ListProperty<String> = project.objects.listProperty<String>()
        .convention(defaultPackagingExtension.javaArgs)

    override val mainClass: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.mainClass)

    override val mainJar: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.mainJar)

    override val mainModule: Property<String> = project.objects.property<String>()
        .convention(defaultPackagingExtension.mainModule)
    //endregion

    //region Options for creating the application package
    override val appImage: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.appImage)

    override val fileAssociations: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.fileAssociations)

    override val installDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(defaultPackagingExtension.installDirectory)

    override val license: RegularFileProperty = project.objects.fileProperty()
        .convention(defaultPackagingExtension.license)

    override val resourcesDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(defaultPackagingExtension.resourcesDirectory)
    //endregion
}
