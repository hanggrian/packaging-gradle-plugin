package com.hanggrian.packaging

import org.gradle.api.Action

/**
 * Extension instance when configuring `packaging` in Gradle scripts. Configuration of
 * this [PackSpec] will be passed into platform-specific specs.
 */
@PackConfigurationDsl
public interface PackagingExtension : PackSpec {
    /** Windows options that will be packaged. */
    public val windows: WindowsOptions

    /** Mac options that will be packaged. */
    public val mac: MacOptions

    /** Linux options that will be packaged. */
    public val linux: LinuxOptions

    /** Configures the windows options that will be packaged. */
    public fun windows(action: Action<in WindowsOptions>)

    /** Configures the Mac options that will be packaged. */
    public fun mac(action: Action<in MacOptions>)

    /** Configures the linux options that will be packaged. */
    public fun linux(action: Action<in LinuxOptions>)
}
