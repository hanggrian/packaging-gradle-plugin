package com.hendraanggrian.packaging

import org.gradle.api.model.ObjectFactory

/**
 * Platform-specific options than can be configured using [PackagingExtension.windows].
 * This [PackSpec] will also inherit configuration from extension.
 */
interface WindowsOptions : PackSpec {
    //region Platform dependent option for creating the application launcher
    /** Creates a console launcher for the application, should be specified for application which requires console interactions. */
    var console: Boolean
    //endregion

    //region Platform dependent options for creating the application package
    /** Adds a dialog to enable the user to choose a directory in which the product is installed. */
    var directoryChooser: Boolean

    /** Adds the application to the system menu. */
    var menu: Boolean

    /** Start Menu group this application is placed in. */
    var menuGroup: String?

    /** Request to perform an install on a per-user basis. */
    var perUserInstall: Boolean

    /** Creates a desktop shortcut for the application. */
    var shortcut: Boolean

    /** UUID associated with upgrades for this package. */
    var upgradeUUID: String?
    //endregion
}

internal class WindowsOptionsImpl(objects: ObjectFactory, defaultPackSpec: PackSpec) :
    PlatformOptionsImpl(objects, defaultPackSpec), WindowsOptions {
    //region Platform dependent option for creating the application launcher
    override var console: Boolean = false
    //endregion

    //region Platform dependent options for creating the application package
    override var directoryChooser: Boolean = false
    override var menu: Boolean = false
    override var menuGroup: String? = null
    override var perUserInstall: Boolean = false
    override var shortcut: Boolean = false
    override var upgradeUUID: String? = null
    //endregion
}
