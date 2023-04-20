package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.LinuxOptions
import com.hendraanggrian.packaging.LinuxOptionsImpl
import com.hendraanggrian.packaging.MacOptions
import com.hendraanggrian.packaging.MacOptionsImpl
import com.hendraanggrian.packaging.PackagingExtension
import com.hendraanggrian.packaging.WindowsOptions
import com.hendraanggrian.packaging.WindowsOptionsImpl
import org.gradle.api.Action
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
import java.io.File

open class DefaultPackagingExtension(private val objects: ObjectFactory, layout: ProjectLayout) :
    PackagingExtension {

    var windowsOptions: WindowsOptions? = null
    var macOptions: MacOptions? = null
    var linuxOptions: LinuxOptions? = null

    override val windows: WindowsOptions
        get() {
            if (windowsOptions == null) {
                windowsOptions = WindowsOptionsImpl(objects, this)
            }
            return windowsOptions!!
        }

    override fun windows(action: Action<in WindowsOptions>): Unit = action.execute(windows)

    override val mac: MacOptions
        get() {
            if (macOptions == null) {
                macOptions = MacOptionsImpl(objects, this)
            }
            return macOptions!!
        }

    override fun mac(action: Action<in MacOptions>): Unit = action.execute(mac)

    override val linux: LinuxOptions
        get() {
            if (linuxOptions == null) {
                linuxOptions = LinuxOptionsImpl(objects, this)
            }
            return linuxOptions!!
        }

    override fun linux(action: Action<in LinuxOptions>): Unit = action.execute(linux)

    //region Generic Options
    final override val appVersion: Property<String> = objects.property()

    final override val copyright: Property<String> = objects.property()

    final override val appDescription: Property<String> = objects.property()

    final override val appName: Property<String> = objects.property()

    final override val outputDirectory: DirectoryProperty = objects.directoryProperty()
        .convention(layout.buildDirectory.dir("install"))

    final override val vendor: Property<String> = objects.property()

    final override val verbose: Property<Boolean> = objects.property<Boolean>()
        .convention(false)
    //endregion

    //region Options for creating the runtime image
    final override val modules: SetProperty<String> = objects.setProperty()

    final override val modulePaths: SetProperty<File> = objects.setProperty()

    final override val bindServices: Property<String> = objects.property()

    final override val runtimeImage: RegularFileProperty = objects.fileProperty()
    //endregion

    //region Options for creating the application image
    final override val icon: RegularFileProperty = objects.fileProperty()

    final override val inputDirectory: DirectoryProperty = objects.directoryProperty()
    //endregion

    //region Options for creating the application launcher(s)
    final override val launcher: RegularFileProperty = objects.fileProperty()

    final override val args: ListProperty<String> = objects.listProperty()

    final override val javaArgs: ListProperty<String> = objects.listProperty()

    final override val mainClass: Property<String> = objects.property()

    final override val mainJar: Property<String> = objects.property()

    final override val mainModule: Property<String> = objects.property()
    //endregion

    //region Options for creating the application package
    final override val appImage: RegularFileProperty = objects.fileProperty()

    final override val fileAssociations: RegularFileProperty = objects.fileProperty()

    final override val installDirectory: DirectoryProperty = objects.directoryProperty()

    final override val license: RegularFileProperty = objects.fileProperty()

    final override val resourcesDirectory: DirectoryProperty = objects.directoryProperty()
    //endregion
}
