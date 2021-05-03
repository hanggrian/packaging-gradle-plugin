include("packaging-gradle-plugin")
include("website")
//includeDir("example")

fun includeDir(name: String) = file(name)
    .listFiles()!!
    .filter { it.isDirectory }
    .forEach { include("$name:${it.name}") }
