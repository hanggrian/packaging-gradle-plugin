package com.hendraanggrian.packr

/** Removes leading slash from this text (if any). */
inline val String.withoutLeadingSlash: String get() = if (startsWith('/')) substring(1) else this

/** Returns not empty text or null instead. */
inline val String.notEmptyOrNull: String? get() = if (isEmpty()) null else this
