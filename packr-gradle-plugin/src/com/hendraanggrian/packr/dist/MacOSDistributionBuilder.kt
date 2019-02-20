package com.hendraanggrian.packr.dist

import java.io.File

@DistributionBuilderMarker
interface MacOSDistributionBuilder : DistributionBuilder {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: File?

    /** Convenient method to set icon resource from file path, relative to project directory. */
    fun icon(value: String) {
        icon = project.projectDir.resolve(value)
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String?

    /** Groovy-friendly method. */
    fun bundleId(value: String) {
        bundleId = value
    }
}