package com.hendraanggrian.packr.dist

import com.hendraanggrian.packr.VmArged

@DistributionBuilderMarker
interface DistributionBuilder : VmArged {

    /** File name of this distribution that will be generated. */
    var name: String?

    fun name(value: String) {
        name = value
    }

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String?

    fun jdk(value: String) {
        jdk = value
    }
}