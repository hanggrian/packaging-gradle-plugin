import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
}

sourceSets["main"].java.srcDir("src")