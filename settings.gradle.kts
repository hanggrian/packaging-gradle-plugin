include(RELEASE_ARTIFACT)
include("website")
includeDir("demo")

fun includeDir(name: String) = file(name)
    .listFiles()
    .filter { it.isDirectory }
    .forEach { include("$name:${it.name}") }