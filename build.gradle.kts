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
        classpath(junitPlatform("gradle-plugin"))
    }
}

allprojects {
    repositories {
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots") // packr 2.1-SNAPSHOT
    }
    tasks.withType(Delete::class.java) {
        delete(projectDir.resolve("out"))
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
    register("wrapper", Wrapper::class) {
        gradleVersion = VERSION_GRADLE
    }
}