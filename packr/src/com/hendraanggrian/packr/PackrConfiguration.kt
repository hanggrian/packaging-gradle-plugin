package com.hendraanggrian.packr

import java.io.File

/**
 * Base configuration for packr task and extension.
 *
 * @see PackrExtension
 * @see PackTask
 */
interface PackrConfiguration {

    /**
     * Directory, ZIP file, or URL to ZIP file of an OpenJDK or Oracle JDK build containing a JRE.
     * Must be defined or will throw an exception.
     */
    var jdk: String?

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    var executable: String?

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    var classpath: MutableList<String>

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Must be defined or will throw an exception.
     */
    var mainClass: String?

    /**
     * List of arguments for the JVM, without leading dashes, e.g. `Xmx1G`.
     * Default is empty.
     */
    var vmArgs: MutableList<String>

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    var resources: MutableList<File>

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is `soft`.
     */
    var minimizeJre: String?

    /**
     * The output name.
     * Default is project's name.
     */
    var outputName: String?

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    var outputDir: File?

    /**
     * Location of an AppBundle icon resource (.icns file).
     * This is an optional property.
     */
    var iconDir: File?

    /**
     * The bundle identifier of your Java application, e.g. `com.my.app`.
     * This is an optional property.
     */
    var bundleId: String?

    /**
     * Print extra messages about JRE minimization when set to `true`.
     * This is an optional property.
     */
    var verbose: Boolean?

    /**
     * Open [outputDir] upon packing completion.
     * This is an optional property.
     */
    var openOnDone: Boolean?

    /** Add absolute file locations of the JAR files to package. */
    fun classpath(vararg jars: String) {
        classpath.addAll(jars)
    }

    /** Add absolute file locations of the JAR files to package. */
    fun classpath(vararg jars: File) {
        classpath.addAll(jars.map { it.path })
    }

    /** Add VM arguments. */
    fun vmArgs(vararg args: String) {
        vmArgs.addAll(args)
    }

    /** Add relative resources files or directories. */
    fun resources(vararg files: String) {
        resources.addAll(files.map { File(it) })
    }

    /** Add absolute resources files or directories. */
    fun resources(vararg files: File) {
        resources.addAll(files)
    }
}