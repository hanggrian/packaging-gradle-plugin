package com.hanggrian.packaging

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
import java.io.File

/** Reverse the effect of [kr.motd.maven.os.Detector.normalizeArch]. */
internal fun denormalizeArch(value: String): String =
    when {
        value.startsWith("x86_64") -> "x64"
        value.startsWith("x86_32") -> "x32"
        value.startsWith("itanium_64") -> "itanium64"
        value.startsWith("itanium_32") -> "itanium32"
        value.startsWith("sparc_32") -> "sparc32"
        value.startsWith("sparc_64") -> "sparc64"
        value.startsWith("arm_32") -> "arm32"
        value.startsWith("aarch_64") -> "aarch64"
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

internal abstract class PlatformOptionsImpl(objects: ObjectFactory, defaultPackSpec: PackSpec) :
    PackSpec {
    //region Generic Options
    override val appVersion: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.appVersion)

    override val copyright: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.copyright)

    override val appDescription: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.appDescription)

    override val appName: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.appName)

    override val outputDirectory: DirectoryProperty =
        objects
            .directoryProperty()
            .convention(defaultPackSpec.outputDirectory)

    override val vendor: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.vendor)

    override val verbose: Property<Boolean> =
        objects
            .property<Boolean>()
            .convention(defaultPackSpec.verbose)
    //endregion

    //region Options for creating the runtime image
    override val modules: SetProperty<String> =
        objects
            .setProperty<String>()
            .convention(defaultPackSpec.modules)

    override val modulePaths: SetProperty<File> =
        objects
            .setProperty<File>()
            .convention(defaultPackSpec.modulePaths)

    override val bindServices: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.bindServices)

    override val runtimeImage: RegularFileProperty =
        objects
            .fileProperty()
            .convention(defaultPackSpec.runtimeImage)
    //endregion

    //region Options for creating the application image
    override val icon: RegularFileProperty =
        objects
            .fileProperty()
            .convention(defaultPackSpec.icon)

    override val inputDirectory: DirectoryProperty =
        objects
            .directoryProperty()
            .convention(defaultPackSpec.inputDirectory)
    //endregion

    //region Options for creating the application launcher(s)
    override val launcher: RegularFileProperty =
        objects
            .fileProperty()
            .convention(defaultPackSpec.launcher)

    override val args: ListProperty<String> =
        objects
            .listProperty<String>()
            .convention(defaultPackSpec.args)

    override val javaArgs: ListProperty<String> =
        objects
            .listProperty<String>()
            .convention(defaultPackSpec.javaArgs)

    override val mainClass: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.mainClass)

    override val mainJar: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.mainJar)

    override val mainModule: Property<String> =
        objects
            .property<String>()
            .convention(defaultPackSpec.mainModule)
    //endregion

    //region Options for creating the application package
    override val appImage: RegularFileProperty =
        objects
            .fileProperty()
            .convention(defaultPackSpec.appImage)

    override val fileAssociations: RegularFileProperty =
        objects
            .fileProperty()
            .convention(defaultPackSpec.fileAssociations)

    override val installDirectory: DirectoryProperty =
        objects
            .directoryProperty()
            .convention(defaultPackSpec.installDirectory)

    override val license: RegularFileProperty =
        objects
            .fileProperty()
            .convention(defaultPackSpec.license)

    override val resourcesDirectory: DirectoryProperty =
        objects
            .directoryProperty()
            .convention(defaultPackSpec.resourcesDirectory)
    //endregion
}
