include(RELEASE_ARTIFACT)

File("demo")
    .walk()
    .filter { it.isDirectory }
    .forEach { include("demo:${it.name}") }