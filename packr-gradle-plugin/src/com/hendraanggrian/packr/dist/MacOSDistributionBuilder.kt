package com.hendraanggrian.packr.dist

@DistributionBuilderMarker
interface MacOSDistributionBuilder : DistributionBuilder {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: String?

    fun icon(value: String) {
        icon = value
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String?

    fun bundleId(value: String) {
        bundleId = value
    }
}