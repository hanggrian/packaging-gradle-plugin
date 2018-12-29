package com.hendraanggrian.packr.dist

import com.hendraanggrian.packr.VmArged

@DistributionBuilderMarker
interface DistributionBuilder : VmArged {

    /** File name of this distribution that will be generated. */
    var name: String?

    /** Groovy-specific method to change name. */
    fun name(name: String) {
        this.name = name
    }

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE used to build
     * this distribution.
     * Default is Java Home path, if any.
     */
    var jdk: String?

    /** Groovy-specific method to change jdk path. */
    fun jdk(jdk: String) {
        this.jdk = jdk
    }
}