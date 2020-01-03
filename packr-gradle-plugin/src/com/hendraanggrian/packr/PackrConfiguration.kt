package com.hendraanggrian.packr

import java.io.File

/**
 * Marks a class with configurable packr commands.
 *
 * @see PackrExtension
 * @see PackTask
 */
internal interface PackrConfiguration : VmArged {

    /** Working directory of a [org.gradle.api.Project]. */
    val projectDir: File

    /**
     * Name of the native executable, without extension such as `.exe`.
     * Default is project's name.
     */
    var executable: String?

    /**
     * File locations of the JAR files to package.
     * Default is empty.
     */
    var classpath: Iterable<File>

    /** Convenient method to set classpath from file path, relative to project directory. */
    fun classpath(vararg relativePaths: String) {
        classpath = relativePaths.map { projectDir.resolve(it) }
    }

    /**
     * File locations of JAR files to remove native libraries which do not match the target platform.
     * Default is empty.
     */
    var removePlatformLibs: Iterable<File>

    /** Convenient method to set remove platform libraries from file path, relative to project directory. */
    fun removePlatformLibs(vararg relativePaths: String) {
        removePlatformLibs = relativePaths.map { projectDir.resolve(it) }
    }

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Must be defined or will throw an exception.
     */
    var mainClass: String?

    /**
     * List of files and directories to be packaged next to the native executable.
     * Default is empty.
     */
    var resources: Iterable<File>

    /** Convenient method to set resources from file path, relative to project directory. */
    fun resources(vararg relativePaths: String) {
        resources = relativePaths.map { projectDir.resolve(it) }
    }

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is [PackrExtension.MINIMIZATION_SOFT].
     */
    var minimizeJre: String

    /**
     * The output directory.
     * Default is `release` directory in project's build directory.
     */
    var outputDir: File

    /** Convenient method to set output directory from file path, relative to project directory. */
    var outputDirectory: String
        get() = outputDir.absolutePath
        set(value) {
            outputDir = projectDir.resolve(value)
        }

    /**
     * An optional directory to cache the result of JRE extraction and minimization.
     * Default is disabled.
     */
    var cacheJreDir: File?

    /** Convenient method to set JRE cache directory from file path, relative to project directory. */
    var cacheJreDirectory: String?
        get() = cacheJreDir?.absolutePath
        set(value) {
            cacheJreDir = value?.let { projectDir.resolve(it) }
        }

    /** An optional property which, when enabled, prints extra messages about JRE minimization. */
    var isVerbose: Boolean

    /** An optional property which, when enabled, opens [outputDir] upon packing completion. */
    var isAutoOpen: Boolean
}
