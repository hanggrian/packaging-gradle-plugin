internal typealias Plugins = org.gradle.plugin.use.PluginDependenciesSpec
internal typealias Dependencies = org.gradle.api.artifacts.dsl.DependencyHandler

val Dependencies.`gradle-publish` get() = "com.gradle.publish:plugin-publish-plugin:0.14.0"
val Plugins.`gradle-publish` get() = id("com.gradle.plugin-publish")

const val VERSION_KOTLIN = "1.4.32"
const val VERSION_COROUTINES = "1.4.3"
val Dependencies.dokka get() = "org.jetbrains.dokka:dokka-gradle-plugin:1.4.30"
val Plugins.dokka get() = id("org.jetbrains.dokka")
fun Dependencies.kotlinx(module: String, version: String? = null) =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$it" }.orEmpty()}"

val Dependencies.`git-publish` get() = "org.ajoberstar:gradle-git-publish:2.1.3"
val Plugins.`git-publish` get() = id("org.ajoberstar.git-publish")

const val VERSION_TRUTH = "1.0.1"
fun Dependencies.google(module: String, version: String) = "com.google.$module:$module:$version"

fun Dependencies.packr() = "com.badlogicgames.packr:packr:2.1-SNAPSHOT"