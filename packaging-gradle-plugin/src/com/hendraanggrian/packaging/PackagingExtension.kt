package com.hendraanggrian.packaging

import org.gradle.api.provider.Property

/**
 * Extension class to be invoked when `packr { ... }` is defined within project.
 * It contains global packaging configuration, any changes made here will be applied to all [PackTask].
 */
interface PackagingExtension : PackSpec {

    /** An optional property which, when enabled, prints extra messages about JRE minimization. */
    val verbose: Property<Boolean>

    /** An optional property which, when enabled, opens [outputDirectory] upon packing completion. */
    val autoOpen: Property<Boolean>
}
