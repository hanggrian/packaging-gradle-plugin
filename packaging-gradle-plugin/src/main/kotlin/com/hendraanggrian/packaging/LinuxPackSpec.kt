package com.hendraanggrian.packaging

import org.gradle.api.provider.Property

/** Platform-specific options than can be configured using `linux { ... }` within [PackSpec]. */
@PackSpecDsl
interface LinuxPackSpec : PackSpec {
    //region Platform dependent options for creating the application package
    /** Name for Linux package, defaults to the application name. */
    val packageName: Property<String>

    /** Maintainer for .deb bundle. */
    val debMaintainer: Property<String>

    /** Menu group this application is placed in. */
    val menuGroup: Property<String>

    /** Required packages or capabilities for the application. */
    val packageDependencies: Property<String>

    /** Type of the license ("License: " of the RPM .spec). */
    val rpmLicenseType: Property<String>

    /** Release value of the RPM .spec file or Debian revision value of the DEB control file. */
    val appRelease: Property<String>

    /** Group value of the RPM .spec file or Section value of DEB control file. */
    val appCategory: Property<String>

    /** Creates a shortcut for the application. */
    val shortcut: Property<Boolean>
    //endregion
}
