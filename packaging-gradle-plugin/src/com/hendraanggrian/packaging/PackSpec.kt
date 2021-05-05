package com.hendraanggrian.packaging

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import java.io.File

/** A convention used by [PackagingExtension] and [PackTask]. */
interface PackSpec {

    /**
     * File name of this distribution that will be generated.
     * Default is project's name.
     */
    val appName: Property<String>

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    val executable: Property<String>

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    val classpath: DirectoryProperty

    /**
     * File locations of JAR files to remove native libraries which do not match the target platform.
     * Default is empty.
     */
    val removePlatformLibraries: ListProperty<File>

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Must be defined or will throw an exception.
     */
    val mainClass: Property<String>

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    val resources: ListProperty<File>

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is [PackagingExtension.MINIMIZATION_SOFT].
     */
    val minimizeJre: Property<String>

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    val outputDirectory: DirectoryProperty

    /**
     * An optional directory to cache the result of JRE extraction and minimization.
     * Default is disabled.
     */
    val cacheJreDirectory: DirectoryProperty

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    val vmArgs: ListProperty<String>
}
