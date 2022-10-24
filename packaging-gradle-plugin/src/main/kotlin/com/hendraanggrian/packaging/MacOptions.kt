package com.hendraanggrian.packaging

import org.gradle.api.model.ObjectFactory
import java.io.File

/**
 * Platform-specific options than can be configured using [PackagingExtension.mac]. This [PackSpec] will also inherit
 * configuration from extension.
 */
interface MacOptions : PackSpec {
    //region Platform dependent option for creating the application launcher
    /** An identifier that uniquely identifies the application for macOSX. */
    var packageIdentifier: String?

    /** Name of the application as it appears in the Menu Bar. */
    var packageName: String?

    /**
     * When signing the application bundle, this value is prefixed to all components that need to be signed that don't
     * have an existing bundle identifier.
     */
    var bundleSigningPrefix: String?

    /** Request that the bundle be signed. */
    var sign: Boolean

    /** Path of the keychain to search for the signing identity. */
    var signingKeychain: File?

    /** Team name portion in Apple signing identities' names. */
    var signingKeyUserName: String?
    //endregion
}

internal class MacOptionsImpl(objects: ObjectFactory, defaultPackSpec: PackSpec) :
    PlatformOptionsImpl(objects, defaultPackSpec), MacOptions {
    //region Platform dependent option for creating the application launcher
    override var packageIdentifier: String? = null
    override var packageName: String? = null
    override var bundleSigningPrefix: String? = null
    override var sign: Boolean = false
    override var signingKeychain: File? = null
    override var signingKeyUserName: String? = null
    //endregion
}
