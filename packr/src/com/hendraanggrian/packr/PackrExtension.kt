package com.hendraanggrian.packr

import kotlin.DeprecationLevel.ERROR

open class PackrExtension {

    internal val _jdks: MutableList<String> = mutableListOf()
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

    var jdks: MutableList<String>
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _jdks += value
        }

    fun jdk(vararg jdks: String) {
        _jdks += jdks
    }

    var executable: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _executable = value
        }

    var classpath: MutableList<String>
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _classpath += value
        }

    fun classpath(vararg jars: String) {
        _classpath += jars
    }

    var mainClass: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _mainClass = value
        }

    var vmArgs: MutableList<String>
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _vmArgs += value
        }

    fun vmArgs(vararg args: String) {
        _vmArgs += args
    }

    var resources: MutableList<String>
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _resources += value
        }

    fun resources(vararg resources: String) {
        _resources += resources
    }

    var minimizeJre: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _minimizeJre = value
        }

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

    var macIcon: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _macIcon = value
        }

    var macBundle: String
        @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
        set(value) {
            _macBundle = value
        }

    companion object {
        private const val NO_GETTER: String = "Property does not have a getter"

        private fun noGetter(): Nothing = throw UnsupportedOperationException(NO_GETTER)
    }
}