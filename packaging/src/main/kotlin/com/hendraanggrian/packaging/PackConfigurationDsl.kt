package com.hendraanggrian.packaging

/**
 * Forces platform packaging configurations to be on the same level, such as:
 *
 * ```
 * packaging {
 *     windows { }
 *     mac { }
 *     linux { }
 * }
 * ```
 */
@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class PackConfigurationDsl
