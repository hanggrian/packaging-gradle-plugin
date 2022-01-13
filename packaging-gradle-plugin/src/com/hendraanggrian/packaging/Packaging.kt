package com.hendraanggrian.packaging

import org.gradle.api.Action
import org.gradle.api.provider.Property

/** Extension class to be invoked when `packaging { ... }` is defined within project. */
@PackSpecMarker
interface Packaging : PackSpec {

    /** Windows platform options. */
    val windowsSpec: Property<WindowsPackSpec>

    /** macOS platform options. */
    val macSpec: Property<MacPackSpec>

    /** Linux platform options. */
    val linuxSpec: Property<LinuxPackSpec>

    /** Invoke a configurator block to modify [WindowsPackSpec]. */
    fun windows(action: Action<WindowsPackSpec>) {
        action.execute(windowsSpec.get())
    }

    /** Invoke a configurator block to modify [MacPackSpec]. */
    fun mac(action: Action<MacPackSpec>) {
        action.execute(macSpec.get())
    }

    /** Invoke a configurator block to modify [LinuxPackSpec]. */
    fun linux(action: Action<LinuxPackSpec>) {
        action.execute(linuxSpec.get())
    }
}
