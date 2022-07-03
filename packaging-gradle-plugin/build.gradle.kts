plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(plugs.plugins.kotlin.jvm)
    alias(plugs.plugins.dokka)
    alias(plugs.plugins.spotless)
    alias(plugs.plugins.gradle.publish)
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
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(sdk.versions.jdk.get()))
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
    testImplementation(testLibs.kotlin.junit)
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka"))
}
