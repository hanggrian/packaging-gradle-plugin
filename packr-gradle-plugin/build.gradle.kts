group = RELEASE_GROUP
version = RELEASE_VERSION

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    dokka
    `gradle-publish`
}

sourceSets {
    main {
        java.srcDir("src")
    }
    register("functionalTest") {
        java.srcDir("functional-tests/src")
        resources.srcDir("functional-tests/res")
        compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath
        runtimeClasspath += output + compileClasspath
    }
}

gradlePlugin {
    testSourceSets(sourceSets["functionalTest"])
}

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(packr())
    testImplementation(kotlin("test-junit", VERSION_KOTLIN))
    "functionalTestImplementation"(gradleTestKit())
    "functionalTestImplementation"(kotlin("test-junit", VERSION_KOTLIN))
}

ktlint()

tasks {
    val deploy by registering {
        dependsOn("build")
        projectDir.resolve("build/libs").listFiles()?.forEach {
            it.renameTo(File(rootDir.resolve("example"), it.name))
        }
    }

    val functionalTest by registering(Test::class) {
        description = "Runs the functional tests."
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        testClassesDirs = sourceSets["functionalTest"].output.classesDirs
        classpath = sourceSets["functionalTest"].runtimeClasspath
        mustRunAfter(test)
    }
    check { dependsOn(functionalTest) }
}

publishPlugin(
    "Packr Gradle Plugin",
    "$RELEASE_GROUP.$RELEASE_ARTIFACT.PackrPlugin"
)