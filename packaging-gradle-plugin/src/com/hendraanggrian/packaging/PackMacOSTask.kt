package com.hendraanggrian.packaging

import com.hendraanggrian.packaging.internal.AbstractPackTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property

open class PackMacOSTask : AbstractPackTask() {

    /** Create `pkg` installer instead of the default `dmg`. */
    @Input
    val usePKG: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)

    //region Platform dependent option for creating the application launcher
    /** An identifier that uniquely identifies the application for macOSX. */
    @Optional
    @Input
    val packageIdentifier: Property<String> = project.objects.property()

    /** Name of the application as it appears in the Menu Bar. */
    @Optional
    @Input
    val packageName: Property<String> = project.objects.property()

    /** When signing the application bundle, this value is prefixed to all components that need to be signed that don't have an existing bundle identifier. */
    @Optional
    @Input
    val bundleSigningPrefix: Property<String> = project.objects.property()

    /** Request that the bundle be signed. */
    @Optional
    @Input
    val sign: Property<Boolean> = project.objects.property()

    /** Path of the keychain to search for the signing identity. */
    @Optional
    @Input
    val signingKeychain: RegularFileProperty = project.objects.fileProperty()

    /** Team name portion in Apple signing identities' names. */
    @Optional
    @Input
    val signingKeyUserName: Property<String> = project.objects.property()
    //endregion

    override val platformDependentLines: List<String>
        get() {
            val lines = mutableListOf("--type", if (usePKG.isPresent && usePKG.get()) "pkg" else "dmg")
            packageIdentifier.orNull?.let {
                lines += "--mac-package-identifier"
                lines += it
            }
            packageName.orNull?.let {
                lines += "--mac-package-name"
                lines += it
            }
            bundleSigningPrefix.orNull?.let {
                lines += "--mac-bundle-signing-prefix"
                lines += it
            }
            if (sign.isPresent && sign.get()) {
                lines += "--mac-sign"
            }
            signingKeychain.orNull?.let {
                lines += "--mac-signing-keychain"
                lines += it.asFile.absolutePath
            }
            signingKeyUserName.orNull?.let {
                lines += "--mac-signing-key-user-name"
                lines += it
            }
            return lines
        }
}
