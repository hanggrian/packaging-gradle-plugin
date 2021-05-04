package com.hendraanggrian.packaging

import org.gradle.api.provider.Property

/**
 * Marks a class with configurable packr commands.
 * @see PackagingExtension
 * @see PackTask
 */
interface PackagingExtension : PackSpec {

    /** An optional property which, when enabled, prints extra messages about JRE minimization. */
    val verbose: Property<Boolean>

    /** An optional property which, when enabled, opens [outputDirectory] upon packing completion. */
    val autoOpen: Property<Boolean>
}
