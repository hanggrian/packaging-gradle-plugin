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
        compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath
        runtimeClasspath += output + compileClasspath
    }
}

gradlePlugin {
    plugins {
        val packagingPlugin by plugins.registering {
            id = "$RELEASE_GROUP.packaging"
            implementationClass = "$RELEASE_GROUP.packaging.PackagingPlugin"
            displayName = "Packaging plugin"
            description = RELEASE_DESCRIPTION
        }
    }
    testSourceSets(sourceSets["functionalTest"])
}

ktlint()

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(osdetector())
    testImplementation(kotlin("test-junit", VERSION_KOTLIN))
    "functionalTestImplementation"(gradleTestKit())
    "functionalTestImplementation"(kotlin("test-junit", VERSION_KOTLIN))
}

tasks {
    val functionalTest by registering(Test::class) {
        description = "Runs the functional tests."
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        testClassesDirs = sourceSets["functionalTest"].output.classesDirs
        classpath = sourceSets["functionalTest"].runtimeClasspath
        mustRunAfter(test)
    }
    check { dependsOn(functionalTest) }

    dokkaHtml {
        outputDirectory.set(buildDir.resolve("dokka/$RELEASE_ARTIFACT"))
    }
}

pluginBundle {
    website = RELEASE_GITHUB
    vcsUrl = "$RELEASE_GITHUB.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("packaging", "packr", "jar")
    mavenCoordinates {
        groupId = RELEASE_GROUP
        artifactId = RELEASE_ARTIFACT
    }
}