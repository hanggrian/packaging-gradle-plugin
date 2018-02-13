const val releaseGroup = "com.hendraanggrian"
const val releaseArtifact = "packr"
const val releaseDebug = true
const val releaseVersion = "0.1"

const val kotlinVersion = "1.2.21"
const val coroutinesVersion = "0.22.2"

fun Dependency.kotlinx(module: String, version: String? = null) = "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" }
    ?: ""}"

fun Dependency.hendraanggrian(module: String, version: String) = "com.hendraanggrian:$module:$version"
inline val Plugin.r get() = id("r")
inline val Plugin.buildconfig get() = id("buildconfig")
const val rVersion = "0.2"
const val buildconfigVersion = "0.8"
const val kotfxVersion = "0.24"

fun Dependency.packr() = "com.badlogicgames.packr:packr:2.0-SNAPSHOT"

fun Dependency.gson() = "com.google.code.gson:gson:2.8.2"

fun Dependency.shadow() = "com.github.jengelman.gradle.plugins:shadow:2.0.2"
inline val Plugin.shadow get() = id("com.github.johnrengelman.shadow")

fun Dependency.ktlint(): org.gradle.api.artifacts.Dependency = add("ktlint", "com.github.shyiko:ktlint:0.15.0")

fun Dependency.junitPlatform(module: String, version: String) = "org.junit.platform:junit-platform-$module:$version"
val Plugin.`junit-platform` get() = id("org.junit.platform.gradle.plugin")
const val junitPlatformVersion = "1.0.0"

fun Dependency.spek(module: String, version: String) = "org.jetbrains.spek:spek-$module:$version"
const val spekVersion = "1.1.5"

private typealias Dependency = org.gradle.api.artifacts.dsl.DependencyHandler
private typealias Plugin = org.gradle.plugin.use.PluginDependenciesSpec