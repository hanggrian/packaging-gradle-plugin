const val VERSION_PACKR = "2.1-SNAPSHOT"

fun org.gradle.api.artifacts.dsl.DependencyHandler.packr() =
    "com.badlogicgames.packr:packr:$VERSION_PACKR"
