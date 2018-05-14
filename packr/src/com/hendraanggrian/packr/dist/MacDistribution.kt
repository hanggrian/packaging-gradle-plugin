package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Project

class MacDistribution(project: Project) : Distribution(Platform.MacOS, project) {

    /**
     * Location of an AppBundle icon resource (.icns file).
     * This is an optional property.
     */
    var iconDir: String? = null

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null
}