@file:Suppress("NOTHING_TO_INLINE", "UnstableApiUsage")

package com.hendraanggrian.packaging.internal

import com.hendraanggrian.packaging.PackagingExtension
import com.hendraanggrian.packaging.PackagingPlugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import java.io.File

/** Extension class to be invoked when `packr { ... }` is defined within project. */
open class DefaultPackagingExtension(project: Project) : PackagingExtension {

    override val appName: Property<String> = project.objects.property<String>()
        .convention(project.name)

    override val executable: Property<String> = project.objects.property<String>()
        .convention(project.name)

    override val classpath: DirectoryProperty = project.objects.directoryProperty()

    override val removePlatformLibraries: ListProperty<File> = project.objects.listProperty<File>()
        .convention(mutableListOf())

    override val mainClass: Property<String> = project.objects.property()

    override val vmArgs: ListProperty<String> = project.objects.listProperty<String>()
        .convention(mutableListOf())

    override val resources: ListProperty<File> = project.objects.listProperty<File>()
        .convention(mutableListOf())

    override val minimizeJre: Property<String> = project.objects.property<String>()
        .convention(PackagingPlugin.MINIMIZATION_SOFT)

    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("install"))

    override val cacheJreDirectory: DirectoryProperty = project.objects.directoryProperty()

    override val verbose: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)

    override val autoOpen: Property<Boolean> = project.objects.property<Boolean>()
        .convention(false)
}
