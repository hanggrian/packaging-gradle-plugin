package com.hendraanggrian.packr

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import java.io.File

interface PackrPlatformConfiguration : VmArged {

    /**
     * File name of this distribution that will be generated.
     * Default is project's name.
     */
    val releaseName: Property<String>

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build
     * containing a JRE used to build this distribution.
     * Default is Java Home environment variable, if any.
     */
    val jdk: Property<String>

    /**
     * Location of an AppBundle icon resource (.icns file) relative to project directory.
     * This is an optional property that is relevant only on macOS.
     */
    val icon: Property<File>

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property that is relevant only on macOS.
     */
    val bundleId: Property<String>
}

/**
 * Marks a class with configurable packr commands.
 * @see PackrExtension
 * @see PackTask
 */
interface PackrGlobalConfiguration : VmArged {

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    val executable: Property<String>

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    val classpath: ListProperty<File>

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
     * Default is [PackrExtension.MINIMIZATION_SOFT].
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

    /** An optional property which, when enabled, prints extra messages about JRE minimization. */
    val verbose: Property<Boolean>

    /** An optional property which, when enabled, opens [outputDirectory] upon packing completion. */
    val autoOpen: Property<Boolean>
}

/**
 * Marks a class with configurable VM arguments.
 * @see Distribution
 * @see PackrExtension
 * @see PackTask
 */
interface VmArged {

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    val vmArgs: ListProperty<String>
}
