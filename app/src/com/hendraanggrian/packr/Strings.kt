package com.hendraanggrian.packr

inline val String.withoutLeadingSlash: String get() = if (startsWith('/')) substring(1) else this

inline val String.notEmptyOrNull: String? get() = if (isEmpty()) null else this