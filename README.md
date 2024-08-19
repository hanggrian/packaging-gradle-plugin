[![CircleCI](https://img.shields.io/circleci/build/gh/hanggrian/packaging-gradle-plugin)](https://app.circleci.com/pipelines/github/hanggrian/packaging-gradle-plugin/)
[![Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/com.hanggrian.packaging)](https://plugins.gradle.org/plugin/com.hanggrian.packaging)
[![Java](https://img.shields.io/badge/java-17+-informational)](https://docs.oracle.com/javase/17/)

# Packaging Gradle Plugin

Gradle plugin that wraps JARs into native bundle for Windows, macOS, and Linux.

- Complete customization for each distribution.
- Pack multiple distributions with a single task.

## Download

Using plugins DSL:

```gradle
plugins {
    id('com.hanggrian.packaging') version "$version"
}
```

Using legacy plugin application:

```gradle
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.hanggrian:packaging-gradle-plugin:$version")
    }
}

apply plugin: 'com.hanggrian.packaging'
```

## Usage

Below are example configuration for `Windows64` and `MacOS` distributions. Note
that properties of distribution configuration may override extension
configuration.

```gradle
packaging {
    appName.set('Custom Directory')
    mainClass.set('com.example.App')
    modules = ['javafx.controls', 'javafx.graphics']
    modulePaths.add(new File('/path/to/javafx-sdk/lib'))
    verbose.set(true)
}
```

### Using [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

For easier setup, also use `application` plugin to distribute classpath
with `installDist` command.

```gradle
apply plugin: 'application'
apply plugin: 'com.hanggrian.packaging'

application {
    applicationName = 'My App'
    mainClass.set('com.example.App')
}
```
