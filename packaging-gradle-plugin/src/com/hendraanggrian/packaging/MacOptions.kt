package com.hendraanggrian.packaging

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

/** Platform-specific options than can be configured using `mac { ... }` within [PackagingExtension]. */
interface MacOptions {

    //region Platform dependent option for creating the application launcher
    /** An identifier that uniquely identifies the application for macOSX. */
    val packageIdentifier: Property<String>

    /** Name of the application as it appears in the Menu Bar. */
    val packageName: Property<String>

    /** When signing the application bundle, this value is prefixed to all components that need to be signed that don't have an existing bundle identifier. */
    val bundleSigningPrefix: Property<String>

    /** Request that the bundle be signed. */
    val sign: Property<Boolean>

    /** Path of the keychain to search for the signing identity. */
    val signingKeychain: RegularFileProperty

    /** Team name portion in Apple signing identities' names. */
    val signingKeyUserName: Property<String>
    //endregion
}
