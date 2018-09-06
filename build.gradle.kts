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
        maven("https://oss.sonatype.org/content/repositories/snapshots") // packr 2.1 is still in snapshot
    }
    tasks.withType(Delete::class.java) {
        delete(projectDir.resolve("out"))
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
    val wrapper by registering(Wrapper::class) {
        gradleVersion = VERSION_GRADLE
    }
}