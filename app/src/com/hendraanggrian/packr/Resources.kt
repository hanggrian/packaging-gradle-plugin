@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.packr

import java.net.URL

inline fun getResources(name: String): URL = App::class.java.getResource(name)