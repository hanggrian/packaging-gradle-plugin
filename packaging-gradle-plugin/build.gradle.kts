plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.publish)
}

kotlin.jvmToolchain(libs.versions.jdk.get().toInt())

gradlePlugin {
    website.set(RELEASE_URL)
    vcsUrl.set("$RELEASE_URL.git")
    plugins.register("packagingPlugin") {
        id = RELEASE_GROUP
        displayName = "Packaging Plugin"
        description = RELEASE_DESCRIPTION
        tags.set(listOf("packaging", "jpackage", "native", "installer", "bundle"))
        implementationClass = "$RELEASE_GROUP.PackagingPlugin"
    }
    testSourceSets(sourceSets.test.get())
}

dependencies {
    ktlint(libs.ktlint, ::configureKtlint)
    ktlint(libs.rulebook.ktlint)
    compileOnly(kotlin("gradle-plugin-api"))
    implementation(gradleKotlinDsl())
    implementation(libs.osdetector)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka/"))
}
