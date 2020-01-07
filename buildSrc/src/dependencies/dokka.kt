private const val VERSION_DOKKA: String = "0.10.0"

fun Dependencies.dokka() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_DOKKA"

val Plugins.dokka get() = id("org.jetbrains.dokka")
