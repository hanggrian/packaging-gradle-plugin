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
import org.gradle.language.base.plugins.LifecycleBasePlugin.*
import org.jetbrains.kotlin.serialization.js.DynamicTypeDeserializer.id

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    dokka
    `git-publish`
    bintray
    `bintray-release`
    `junit-platform`
}

group = RELEASE_GROUP
version = RELEASE_VERSION

java.sourceSets {
    get("main").java.srcDir("src")
    get("test").java.srcDir("tests/src")
}

gradlePlugin {
    (plugins) {
        RELEASE_ARTIFACT {
            id = RELEASE_GROUP
            implementationClass = "$RELEASE_GROUP.PackrPlugin"
        }
    }
}

val ktlint by configurations.creating

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(packr())

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

    ktlint(ktlint())
}

tasks {
    "ktlint"(JavaExec::class) {
        get("check").dependsOn(ktlint)
        group = VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("src/**/*.kt")
    }
    "ktlintFormat"(JavaExec::class) {
        group = "formatting"
        inputs.dir("src")
        outputs.dir("src")
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("-F", "src/**/*.kt")
    }

    val dokka by getting(DokkaTask::class) {
        get("gitPublishCopy").dependsOn(this)
        outputDirectory = "$buildDir/docs"
        doFirst {
            file(outputDirectory).deleteRecursively()
            buildDir.resolve("gitPublish").deleteRecursively()
        }
    }
    gitPublish {
        repoUri = RELEASE_WEBSITE
        branch = "gh-pages"
        contents.from(dokka.outputDirectory)
    }
}

publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = RELEASE_ARTIFACT
    publishVersion = RELEASE_VERSION
    desc = RELEASE_DESC
    website = RELEASE_WEBSITE
}

configure<JUnitPlatformExtension> {
    if (this is ExtensionAware) extensions.getByType(FiltersExtension::class.java).apply {
        if (this is ExtensionAware) extensions.getByType(EnginesExtension::class.java).include("spek")
    }
}
