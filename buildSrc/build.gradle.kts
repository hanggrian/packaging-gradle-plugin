repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("com.gradle.publish:plugin-publish-plugin:0.13.0")
}

plugins {
    `kotlin-dsl`
}

sourceSets {
    main {
        java.srcDir("src")
    }
}