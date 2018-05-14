package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Project

class MacDistribution(project: Project) : Distribution(Platform.MacOS, project) {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: String? = null

    /** Alias for Groovy initialization without operator. */
    fun icon(icon: String?) {
        this.icon = icon
    }

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null

    /** Alias for Groovy initialization without operator. */
    fun bundleId(bundleId: String?) {
        this.bundleId = bundleId
    }
}