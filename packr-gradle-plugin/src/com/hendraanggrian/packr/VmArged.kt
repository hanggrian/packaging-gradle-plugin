package com.hendraanggrian.packr

import org.gradle.api.tasks.Input

internal interface VmArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    val vmArgs: MutableCollection<String>

    /** Groovy-specific method to add vm arguments. */
    @Input fun vmArgs(vararg args: String) {
        vmArgs += args
    }
}