plugins {
    `java-gradle-plugin`
    `kotlin-dsl` version libs.versions.gradle.kotlin.dsl
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.publish)
}

gradlePlugin {
    plugins.register("packagingPlugin") {
        id = "$RELEASE_GROUP.packaging"
        implementationClass = "$id.PackagingPlugin"
        displayName = "Packaging Plugin"
        description = RELEASE_DESCRIPTION
    }
    testSourceSets(sourceSets.test.get())
}

kotlin.jvmToolchain(libs.versions.jdk.get().toInt())

pluginBundle {
    website = RELEASE_URL
    vcsUrl = "$RELEASE_URL.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("packaging", "jpackage", "native", "installer", "bundle")
}

dependencies {
    ktlint(libs.ktlint, ::ktlintConfig)
    ktlint(libs.rulebook.ktlint)
    implementation(libs.osdetector)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka/"))
}
