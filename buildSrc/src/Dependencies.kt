import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec

fun DependencyHandler.dokka() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_DOKKA"
inline val PluginDependenciesSpec.dokka get() = id("org.jetbrains.dokka")

fun DependencyHandler.packr() = "com.badlogicgames.packr:packr:$VERSION_PACKR"

fun DependencyHandler.ktlint() = "com.github.shyiko:ktlint:$VERSION_KTLINT"

fun DependencyHandler.gitPublish() = "org.ajoberstar:gradle-git-publish:$VERSION_GIT_PUBLISH"
inline val PluginDependenciesSpec.`git-publish` get() = id("org.ajoberstar.git-publish")

fun DependencyHandler.bintrayRelease() = "com.novoda:bintray-release:$VERSION_BINTRAY_RELEASE"
inline val PluginDependenciesSpec.`bintray-release` get() = id("com.novoda.bintray-release")

fun DependencyHandler.junitPlatform(module: String) = "org.junit.platform:junit-platform-$module:$VERSION_JUNIT_PLATFORM"
val PluginDependenciesSpec.`junit-platform` get() = id("org.junit.platform.gradle.plugin")

fun DependencyHandler.spek(module: String) = "org.jetbrains.spek:spek-$module:$VERSION_SPEK"