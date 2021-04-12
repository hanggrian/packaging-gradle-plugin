const val VERSION_GRADLE_PUBLISH = "0.13.0"

fun org.gradle.api.artifacts.dsl.DependencyHandler.gradlePublish() =
    "com.gradle.publish:plugin-publish-plugin:$VERSION_GRADLE_PUBLISH"

val org.gradle.plugin.use.PluginDependenciesSpec.`gradle-publish`
    get() = id("com.gradle.plugin-publish")

fun org.gradle.api.Project.publishPlugin(vararg tags: String) {
    checkNotNull(plugins.findPlugin("com.gradle.plugin-publish")) { "Missing plugin `java-gradle-plugin` for this publication" }
    extensions.configure<com.gradle.publish.PluginBundleExtension>("pluginBundle") {
        website = RELEASE_GITHUB
        vcsUrl = RELEASE_GITHUB
        description = RELEASE_DESCRIPTION
        setTags(tags.asList())
    }
}