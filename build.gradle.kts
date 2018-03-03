buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath(dokka())
        classpath(gitPublish())
        classpath(bintrayRelease())
        classpath(junitPlatform("gradle-plugin", junitPlatformVersion))
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

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.5.1"
}

/** bintray upload snippet
./gradlew bintrayUpload -PdryRun=false -PbintrayUser=hendraanggrian -PbintrayKey=
 */