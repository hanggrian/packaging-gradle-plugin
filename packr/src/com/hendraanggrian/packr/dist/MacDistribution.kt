package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Project

class MacDistribution(project: Project, jdk: String) : Distribution(Platform.MacOS, project, jdk) {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: String? = null

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null
}