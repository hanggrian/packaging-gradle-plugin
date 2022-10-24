import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

fun Project.withPlugin(id: String, action: Plugin<*>.() -> Unit) = plugins.withId(id, action)

inline fun <reified T : Plugin<*>> Project.withPlugin(noinline action: T.() -> Unit) =
    plugins.withType<T>().configureEach(action)

inline fun <reified T : Plugin<*>> Project.withPluginEagerly(noinline action: T.() -> Unit) =
    plugins.withType<T>(action)
