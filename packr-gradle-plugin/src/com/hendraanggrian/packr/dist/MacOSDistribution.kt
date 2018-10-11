package com.hendraanggrian.packr.dist

import com.badlogicgames.packr.PackrConfig.Platform
import java.io.File

class MacOSDistribution(name: String) : Distribution(Platform.MacOS, name) {

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property.
     */
    var icon: File? = null

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String? = null
}