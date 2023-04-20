pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
}
dependencyResolutionManagement.repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

rootProject.name = "packaging-gradle-plugin"

include("packaging-gradle-plugin")
include("website")
includeDir("samples") // enabling samples will break Travis because JavaFX SDK is no longer free to download

fun includeDir(dir: String) = file(dir)
    .listFiles()!!
    .filter { it.isDirectory }
    .forEach { include("$dir:${it.name}") }
