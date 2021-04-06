import com.gradle.publish.PluginBundleExtension
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

const val VERSION_GRADLE_PUBLISH = "0.13.0"

fun org.gradle.api.artifacts.dsl.DependencyHandler.gradlePublish() =
    "com.gradle.publish:plugin-publish-plugin:$VERSION_GRADLE_PUBLISH"

val org.gradle.plugin.use.PluginDependenciesSpec.`gradle-publish`
    get() = id("com.gradle.plugin-publish")

fun org.gradle.api.Project.publishPlugin(
    name: String,
    implementation: String,
    vararg tags: String,
    artifact: String = RELEASE_ARTIFACT
) {
    checkNotNull(plugins.findPlugin("java-gradle-plugin")) { "Missing plugin `java-gradle-plugin` for this publication" }
    extensions.configure<GradlePluginDevelopmentExtension>("gradlePlugin") {
        plugins {
            register(artifact) {
                id = "$RELEASE_GROUP.$artifact"
                displayName = name
                description = RELEASE_DESCRIPTION
                implementationClass = implementation
            }
        }
    }
    extensions.configure<PluginBundleExtension>("pluginBundle") {
        website = RELEASE_URL
        vcsUrl = RELEASE_URL
        this.tags = tags.asList()
    }
}