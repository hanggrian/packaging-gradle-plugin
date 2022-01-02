package com.hendraanggrian.packaging

import org.gradle.api.provider.Property

/**
 * Extension class to be invoked when `packr { ... }` is defined within project.
 * It contains global packaging configuration, any changes made here will be applied to all [com.hendraanggrian.packaging.internal.AbstractPackTask].
 */
interface PackagingExtension : PackSpec {

    /** Enables verbose output. */
    val verbose: Property<Boolean>
}
