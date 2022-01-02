package com.hendraanggrian.packaging

import com.hendraanggrian.packaging.internal.AbstractPackTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property

open class PackWindowsTask : AbstractPackTask() {

    /** Create `msi` installer instead of the default `exe`. */
    @Input
    val useMSI: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)

    //region Platform dependent option for creating the application launcher
    /** Creates a console launcher for the application, should be specified for application which requires console interactions. */
    @Optional
    @Input
    val console: Property<Boolean> = project.objects.property()
    //endregion

    //region Platform dependent options for creating the application package
    /** Adds a dialog to enable the user to choose a directory in which the product is installed. */
    @Optional
    @Input
    val directoryChooser: Property<Boolean> = project.objects.property()

    /** Adds the application to the system menu. */
    @Optional
    @Input
    val menu: Property<Boolean> = project.objects.property()

    /** Start Menu group this application is placed in. */
    @Optional
    @Input
    val menuGroup: Property<String> = project.objects.property()

    /** Request to perform an install on a per-user basis. */
    @Optional
    @Input
    val perUserInstall: Property<Boolean> = project.objects.property()

    /** Creates a desktop shortcut for the application. */
    @Optional
    @Input
    val shortcut: Property<Boolean> = project.objects.property()

    /** UUID associated with upgrades for this package. */
    @Optional
    @Input
    val upgradeUUID: Property<String> = project.objects.property()
    //endregion

    override val platformDependentLines: List<String>
        get() {
            val lines = mutableListOf("--type", if (useMSI.isPresent && useMSI.get()) "msi" else "exe")
            if (console.isPresent && console.get()) {
                lines += "--win-console"
            }
            if (directoryChooser.isPresent && directoryChooser.get()) {
                lines += "--win-dir-chooser"
            }
            if (menu.isPresent && menu.get()) {
                lines += "--win-menu"
            }
            menuGroup.orNull?.let {
                lines += "--win-menu-group"
                lines += it
            }
            if (perUserInstall.isPresent && perUserInstall.get()) {
                lines += "--win-per-user-install"
            }
            if (shortcut.isPresent && shortcut.get()) {
                lines += "--win-shortcut"
            }
            upgradeUUID.orNull?.let {
                lines += "--win-upgrade-uuid"
                lines += it
            }
            return lines
        }
}
