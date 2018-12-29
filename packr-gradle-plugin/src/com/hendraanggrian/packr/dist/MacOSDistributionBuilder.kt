package com.hendraanggrian.packr.dist

@DistributionBuilderMarker
interface MacOSDistributionBuilder : DistributionBuilder {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: String?

    /** Groovy-specific method to change icon path. */
    fun icon(icon: String) {
        this.icon = icon
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String?

    /** Groovy-specific method to change bundle identifier. */
    fun bundleId(bundleId: String) {
        this.bundleId = bundleId
    }
}