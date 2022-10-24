plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.spotless)
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

kotlin.jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
}

spotless.kotlin {
    ktlint()
}

pluginBundle {
    website = RELEASE_URL
    vcsUrl = "$RELEASE_URL.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("packaging", "jpackage", "native", "installer", "bundle")
}

dependencies {
    implementation(libs.osdetector)
    testImplementation(gradleTestKit())
    testImplementation(libs.kotlin.test.junit)
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka"))
}
