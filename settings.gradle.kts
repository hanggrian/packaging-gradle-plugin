include("packaging-plugin")
include("website")
includeDir("samples")

fun includeDir(name: String) = file(name)
    .listFiles()!!
    .filter { it.isDirectory }
    .forEach { include("$name:${it.name}") }