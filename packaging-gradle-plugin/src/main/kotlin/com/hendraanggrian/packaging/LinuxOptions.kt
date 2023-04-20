package com.hendraanggrian.packaging

import org.gradle.api.model.ObjectFactory

/**
 * Platform-specific options than can be configured using [PackagingExtension.linux].
 * This [PackSpec] will also inherit configuration from extension.
 */
interface LinuxOptions : PackSpec {
    //region Platform dependent options for creating the application package
    /** Name for Linux package, defaults to the application name. */
    var packageName: String?

    /** Maintainer for .deb bundle. */
    var debMaintainer: String?

    /** Menu group this application is placed in. */
    var menuGroup: String?

    /** Required packages or capabilities for the application. */
    var packageDependencies: String?

    /** Type of the license ("License: " of the RPM .spec). */
    var rpmLicenseType: String?

    /** Release value of the RPM .spec file or Debian revision value of the DEB control file. */
    var appRelease: String?

    /** Group value of the RPM .spec file or Section value of DEB control file. */
    var appCategory: String?

    /** Creates a shortcut for the application. */
    var shortcut: Boolean
    //endregion
}

internal class LinuxOptionsImpl(objects: ObjectFactory, defaultPackSpec: PackSpec) :
    PlatformOptionsImpl(objects, defaultPackSpec), LinuxOptions {

    //region Platform dependent options for creating the application package
    override var packageName: String? = null
    override var debMaintainer: String? = null
    override var menuGroup: String? = null
    override var packageDependencies: String? = null
    override var rpmLicenseType: String? = null
    override var appRelease: String? = null
    override var appCategory: String? = null
    override var shortcut: Boolean = false
    //endregion
}
