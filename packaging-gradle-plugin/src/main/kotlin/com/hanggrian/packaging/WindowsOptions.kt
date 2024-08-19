package com.hanggrian.packaging

import org.gradle.api.model.ObjectFactory

/**
 * Platform-specific options than can be configured using [PackagingExtension.windows].
 * This [PackSpec] will also inherit configuration from extension.
 */
public interface WindowsOptions : PackSpec {
    //region Platform dependent option for creating the application launcher

    /**
     * Creates a console launcher for the application, should be specified for application which
     * requires console interactions.
     */
    public var isConsole: Boolean
    //endregion

    //region Platform dependent options for creating the application package

    /** Adds a dialog to enable the user to choose a directory in which the product is installed. */
    public var isDirectoryChooser: Boolean

    /** Adds the application to the system menu. */
    public var isMenu: Boolean

    /** Start Menu group this application is placed in. */
    public var menuGroup: String?

    /** Request to perform an install on a per-user basis. */
    public var isPerUserInstall: Boolean

    /** Creates a desktop shortcut for the application. */
    public var isShortcut: Boolean

    /** UUID associated with upgrades for this package. */
    public var upgradeUuid: String?
    //endregion
}

internal class WindowsOptionsImpl(objects: ObjectFactory, defaultPackSpec: PackSpec) :
    PlatformOptionsImpl(objects, defaultPackSpec),
    WindowsOptions {
    //region Platform dependent option for creating the application launcher
    override var isConsole: Boolean = false
    //endregion

    //region Platform dependent options for creating the application package
    override var isDirectoryChooser: Boolean = false
    override var isMenu: Boolean = false
    override var menuGroup: String? = null
    override var isPerUserInstall: Boolean = false
    override var isShortcut: Boolean = false
    override var upgradeUuid: String? = null
    //endregion
}
