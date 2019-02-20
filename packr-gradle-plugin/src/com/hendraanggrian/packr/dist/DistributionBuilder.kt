package com.hendraanggrian.packr.dist

import com.hendraanggrian.packr.VmArged
import org.gradle.api.Project

@DistributionBuilderMarker
interface DistributionBuilder : VmArged {

    val project: Project

    /** File name of this distribution that will be generated. */
    var name: String?

    /** Groovy-friendly method. */
    fun name(value: String) {
        name = value
    }

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String?

    /** Groovy-friendly method. */
    fun jdk(value: String) {
        jdk = value
    }
}