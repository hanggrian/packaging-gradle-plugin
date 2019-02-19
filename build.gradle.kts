buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka())
        classpath(gitPublish())
        classpath(bintray())
        classpath(bintrayRelease())
    }
}

allprojects {
    repositories {
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    tasks.withType<Delete> {
        delete(projectDir.resolve("out"))
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
    named<Wrapper>("wrapper") {
        gradleVersion = VERSION_GRADLE
        distributionType = Wrapper.DistributionType.ALL
    }
}