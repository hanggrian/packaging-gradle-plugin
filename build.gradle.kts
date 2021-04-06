buildscript {
    repositories {
        jcenter()
        maven(REPO_GRADLE_PORTAL)
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka())
        classpath(gitPublish())
        classpath(gradlePublish())
    }
}

allprojects {
    repositories {
        jcenter()
        maven(REPO_OSSRH_SNAPSHOTS)
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    tasks.withType<Delete> {
        delete(projectDir.resolve("out"))
    }
}