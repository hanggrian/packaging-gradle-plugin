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
    test {
        java.srcDir("tests/src")
        resources.srcDir("tests/res")
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
    testSourceSets(sourceSets.test.get())
}

ktlint()

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(osdetector())
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", VERSION_KOTLIN))
}

tasks {
    dokkaHtml {
        outputDirectory.set(buildDir.resolve("dokka/$RELEASE_ARTIFACT"))
    }
}

pluginBundle {
    website = RELEASE_GITHUB
    vcsUrl = "$RELEASE_GITHUB.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("packaging", "jpackage", "native", "installer", "bundle")
    mavenCoordinates {
        groupId = RELEASE_GROUP
        artifactId = RELEASE_ARTIFACT
    }
}