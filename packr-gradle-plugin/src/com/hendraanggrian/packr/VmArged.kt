package com.hendraanggrian.packr

interface VmArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: Iterable<String>

    /** Convenient method to set vm arguments. */
    fun vmArgs(vararg args: String) {
        vmArgs = listOf(*args)
    }
}
