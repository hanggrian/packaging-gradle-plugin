package com.hendraanggrian.packr

import kotlin.DeprecationLevel.ERROR

open class PackrExtension {

    val platform: Platform = Platform()
    internal var _executable: String? = null
    internal val _classpath: MutableList<String> = mutableListOf()
    internal var _mainClass: String? = null
    internal val _vmArgs: MutableList<String> = mutableListOf()
    internal val _resources: MutableList<String> = mutableListOf()
    internal var _minimizeJre: String? = null
    internal var _outputDir: String? = null
    internal var _macWrapApp: Boolean = true
    internal var _macIcon: String? = null
    internal var _macBundle: String? = null

    /**
     * Name of the native executable, without extension such as ".exe".
     * Default is project's name.
     */
    var executable: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _executable = value
        }

    /**
     * File locations of the JAR files to package.
     */
    fun classpath(vararg jars: String) {
        _classpath += jars
    }

    /**
     * The fully qualified name of the main class, using dots to delimit package names.
     * Should be defined or will throw an exception.
     */
    var mainClass: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _mainClass = value
        }

    /**
     * List of arguments for the JVM, without leading dashes, e.g. "Xmx1G".
     */
    fun vmArgs(vararg args: String) {
        _vmArgs += args
    }

    /**
     * List of files and directories to be packaged next to the native executable.
     */
    fun resources(vararg resources: String) {
        _resources += resources
    }

    /**
     * Minimize the JRE by removing directories and files as specified by an additional config file.
     * Comes with a few config files out of the box.
     * Default is `soft`.
     */
    var minimizeJre: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _minimizeJre = value
        }

    /**
     * The output directory
     */
    var outputDir: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _outputDir = value
        }

    var macWrapApp: Boolean
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _macWrapApp = value
        }

    /**
     * Location of an AppBundle icon resource (.icns file).
     */
    var macIcon: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _macIcon = value
        }

    /**
     * The bundle identifier of your Java application, e.g. "com.my.app".
     */
    var macBundle: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _macBundle = value
        }

    data class Platform(
        var mac: String? = null,
        var windows32: String? = null,
        var windows64: String? = null,
        var linux32: String? = null,
        var linux64: String? = null
    )

    companion object {
        private const val NO_GETTER: String = "Property does not have a getter"

        private fun noGetter(): Nothing = throw UnsupportedOperationException(NO_GETTER)
    }
}