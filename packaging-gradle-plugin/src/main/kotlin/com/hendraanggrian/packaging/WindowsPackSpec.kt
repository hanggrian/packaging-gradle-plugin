package com.hendraanggrian.packaging

import org.gradle.api.provider.Property

/** Platform-specific options than can be configured using `windows { ... }` within [PackSpec]. */
interface WindowsPackSpec : PackSpec {
    //region Platform dependent option for creating the application launcher
    /** Creates a console launcher for the application, should be specified for application which requires console interactions. */
    val console: Property<Boolean>
    //endregion

    //region Platform dependent options for creating the application package
    /** Adds a dialog to enable the user to choose a directory in which the product is installed. */
    val directoryChooser: Property<Boolean>

    /** Adds the application to the system menu. */
    val menu: Property<Boolean>

    /** Start Menu group this application is placed in. */
    val menuGroup: Property<String>

    /** Request to perform an install on a per-user basis. */
    val perUserInstall: Property<Boolean>

    /** Creates a desktop shortcut for the application. */
    val shortcut: Property<Boolean>

    /** UUID associated with upgrades for this package. */
    val upgradeUUID: Property<String>
    //endregion
}
