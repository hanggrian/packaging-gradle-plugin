buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka)
        classpath(`git-publish`)
        classpath(`gradle-publish`)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}