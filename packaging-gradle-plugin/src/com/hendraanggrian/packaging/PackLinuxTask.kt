package com.hendraanggrian.packaging

import com.hendraanggrian.packaging.internal.AbstractPackTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property

open class PackLinuxTask : AbstractPackTask() {

    /** Create `rpm` installer instead of the default `deb`. */
    @Input
    val useRPM: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)

    //region Platform dependent options for creating the application package
    /** Name for Linux package, defaults to the application name. */
    @Optional
    @Input
    val packageName: Property<String> = project.objects.property()

    /** Maintainer for .deb bundle. */
    @Optional
    @Input
    val debMaintainer: Property<String> = project.objects.property()

    /** Menu group this application is placed in. */
    @Optional
    @Input
    val menuGroup: Property<String> = project.objects.property()

    /** Required packages or capabilities for the application. */
    @Optional
    @Input
    val packageDependencies: Property<String> = project.objects.property()

    /** Type of the license ("License: " of the RPM .spec). */
    @Optional
    @Input
    val rpmLicenseType: Property<String> = project.objects.property()

    /** Release value of the RPM .spec file or Debian revision value of the DEB control file. */
    @Optional
    @Input
    val appRelease: Property<String> = project.objects.property()

    /** Group value of the RPM .spec file or Section value of DEB control file. */
    @Optional
    @Input
    val appCategory: Property<String> = project.objects.property()

    /** Creates a shortcut for the application. */
    @Optional
    @Input
    val shortcut: Property<Boolean> = project.objects.property()
    //endregion

    override val platformDependentLines: List<String>
        get() {
            val lines = mutableListOf("--type", if (useRPM.isPresent && useRPM.get()) "rpm" else "deb")
            packageName.orNull?.let {
                lines += "--linux-package-name"
                lines += it
            }
            debMaintainer.orNull?.let {
                lines += "--linux-deb-maintainer"
                lines += it
            }
            menuGroup.orNull?.let {
                lines += "--linux-menu-group"
                lines += it
            }
            packageDependencies.orNull?.let {
                lines += "--linux-package-deps"
                lines += it
            }
            rpmLicenseType.orNull?.let {
                lines += "--linux-rpm-license-type"
                lines += it
            }
            appRelease.orNull?.let {
                lines += "--linux-app-release"
                lines += it
            }
            appCategory.orNull?.let {
                lines += "--linux-app-category"
                lines += it
            }
            if (shortcut.isPresent && shortcut.get()) {
                lines += "--linux-shortcut"
            }
            return lines
        }
}
