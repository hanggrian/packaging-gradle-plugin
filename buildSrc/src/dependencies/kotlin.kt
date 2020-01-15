const val VERSION_KOTLIN = "1.3.61"
private const val VERSION_DOKKA: String = "0.10.0"

fun Dependencies.dokka() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_DOKKA"

val Plugins.dokka get() = id("org.jetbrains.dokka")