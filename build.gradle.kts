buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka())
        classpath(gitPublish())
    }
}

allprojects {
    repositories {
        jcenter()
        maven(REPOSITORIES_URL_SNAPSHOT)
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    tasks.withType<Delete> {
        delete(projectDir.resolve("out"))
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
