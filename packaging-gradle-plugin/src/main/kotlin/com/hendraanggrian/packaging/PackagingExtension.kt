package com.hendraanggrian.packaging

import org.gradle.api.Action

/**
 * Extension instance when configuring `packaging` in Gradle scripts. Configuration of
 * this [PackSpec] will be passed into platform-specific specs.
 */
@PackConfigurationDsl
interface PackagingExtension : PackSpec {
    /** Windows options that will be packaged. */
    val windows: WindowsOptions

    /** Configures the windows options that will be packaged. */
    fun windows(action: Action<in WindowsOptions>)

    /** Mac options that will be packaged. */
    val mac: MacOptions

    /** Configures the Mac options that will be packaged. */
    fun mac(action: Action<in MacOptions>)

    /** Linux options that will be packaged. */
    val linux: LinuxOptions

    /** Configures the linux options that will be packaged. */
    fun linux(action: Action<in LinuxOptions>)
}
