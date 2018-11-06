package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import java.io.File

class MacOSDistribution : Distribution(Platform.MacOS) {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: File? = null

    /** Groovy-specific method to change icon path. */
    fun icon(icon: File) {
        this.icon = icon
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null

    /** Groovy-specific method to change bundle identifier. */
    fun bundleId(bundleId: String) {
        this.bundleId = bundleId
    }
}