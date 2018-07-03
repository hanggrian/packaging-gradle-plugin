package com.hendraanggrian.packr.internal

internal interface VMArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: MutableList<String>
}