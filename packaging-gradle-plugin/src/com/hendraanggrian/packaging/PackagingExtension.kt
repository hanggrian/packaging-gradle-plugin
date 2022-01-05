package com.hendraanggrian.packaging

import org.gradle.api.Action

/** Extension class to be invoked when `packaging { ... }` is defined within project. */
interface PackagingExtension : Packaging {

    /** Windows platform options. */
    val windowsOptions: WindowsPackaging

    /** macOS platform options. */
    val macOptions: MacPackaging

    /** Linux platform options. */
    val linuxOptions: LinuxPackaging

    /** Invoke a configurator block to modify [WindowsPackaging]. */
    fun windows(action: Action<WindowsPackaging>) {
        action.execute(windowsOptions)
    }

    /** Invoke a configurator block to modify [MacPackaging]. */
    fun mac(action: Action<MacPackaging>) {
        action.execute(macOptions)
    }

    /** Invoke a configurator block to modify [LinuxPackaging]. */
    fun linux(action: Action<LinuxPackaging>) {
        action.execute(linuxOptions)
    }
}
