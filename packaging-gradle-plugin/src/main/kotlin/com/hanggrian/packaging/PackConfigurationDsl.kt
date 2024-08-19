package com.hanggrian.packaging

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
public annotation class PackConfigurationDsl
