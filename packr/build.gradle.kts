import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.`kotlin-dsl`
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.dokka.gradle.DokkaTask

import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    dokka
    `git-publish`
    `bintray-release`
    `junit-platform`
}

group = releaseGroup
version = releaseVersion

java.sourceSets {
    "main" { java.srcDir("src") }
    "test" { java.srcDir("tests/src") }
}

gradlePlugin {
    (plugins) {
        releaseArtifact {
            id = releaseArtifact
            implementationClass = "$releaseGroup.$releaseArtifact.PackrPlugin"
        }
    }
}

val ktlint by configurations.creating

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(packr())
    ktlint(ktlint())
    testImplementation(kotlin("test", VERSION_KOTLIN))
    testImplementation(kotlin("reflect", VERSION_KOTLIN))
    testImplementation(spek("api")) {
        exclude("org.jetbrains.kotlin")
    }
    testRuntime(spek("junit-platform-engine")) {
        exclude("org.jetbrains.kotlin")
        exclude("org.junit.platform")
    }
    testImplementation(junitPlatform("runner"))
}

tasks {
    val ktlint by creating(JavaExec::class) {
        group = "verification"
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = configurations["ktlint"]
        main = "com.github.shyiko.ktlint.Main"
        args("src/**/*.kt")
    }
    get("check").dependsOn(ktlint)
    "ktlintFormat"(JavaExec::class) {
        group = "formatting"
        inputs.dir("src")
        outputs.dir("src")
        description = "Fix Kotlin code style deviations."
        classpath = configurations["ktlint"]
        main = "com.github.shyiko.ktlint.Main"
        args("-F", "src/**/*.kt")
    }

    val dokka by getting(DokkaTask::class) {
        outputDirectory = buildDir.resolve("docs").path
        doFirst {
            file(outputDirectory).deleteRecursively()
            buildDir.resolve("gitPublish").deleteRecursively()
        }
    }

    gitPublish {
        repoUri = releaseWeb
        branch = "gh-pages"
        contents.from(
            "pages",
            dokka.outputDirectory
        )
    }

    get("gitPublishCopy").dependsOn(dokka)
}

publish {
    userOrg = releaseUser
    groupId = releaseGroup
    artifactId = releaseArtifact
    publishVersion = releaseVersion
    desc = releaseDesc
    website = releaseWeb
}

configure<JUnitPlatformExtension> {
    if (this is ExtensionAware) extensions.getByType(FiltersExtension::class.java).apply {
        if (this is ExtensionAware) extensions.getByType(EnginesExtension::class.java).include("spek")
    }
}