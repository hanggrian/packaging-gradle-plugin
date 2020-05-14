package com.hendraanggrian.packr

/**
 * Marks a class with configurable VM arguments.
 *
 * @see Distribution
 * @see PackrExtension
 * @see PackTask
 */
internal interface VmArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: Iterable<String>
}
