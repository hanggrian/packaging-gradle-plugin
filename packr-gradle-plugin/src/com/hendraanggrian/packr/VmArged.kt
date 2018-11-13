package com.hendraanggrian.packr

interface VmArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    val vmArgs: MutableCollection<String>

    /** Groovy-specific method to add vm arguments. */
    fun vmArgs(vararg vmArgs: String) {
        this.vmArgs += vmArgs
    }
}