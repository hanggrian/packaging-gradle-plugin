package com.hendraanggrian.packr

interface VmArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: Iterable<String>

    /** Groovy-specific method to add vm arguments. */
    fun vmArgs(vararg args: String) {
        vmArgs = listOf(*args)
    }
}