[![Travis CI](https://img.shields.io/travis/com/hendraanggrian/packaging-gradle-plugin)](https://travis-ci.com/github/hendraanggrian/packaging-gradle-plugin/)
[![Plugin Portal](https://img.shields.io/maven-metadata/v?label=plugin-portal&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fhendraanggrian%2Fpackaging%2Fcom.hendraanggrian.packaging.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.hendraanggrian.packaging/)
[![OpenJDK](https://img.shields.io/badge/jdk-17+-informational)](https://openjdk.java.net/projects/jdk/17/)

# Packaging Gradle Plugin

Gradle plugin that wraps JARs into native bundle for Windows, macOS, and Linux.

- Complete customization for each distribution.
- Pack multiple distributions with a single task.

| Version | Method                                                                                 |
|---------|----------------------------------------------------------------------------------------|
| 0.1     | [packr](https://github.com/libgdx/packr/)                                               |
| 0.2+    | [jpackage](https://docs.oracle.com/en/java/javase/14/jpackage/packaging-overview.html) |

## Download

Using plugins DSL:

```gradle
plugins {
    id('com.hendraanggrian.packaging') version "$version"
}
```

Using legacy plugin application:

```gradle
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.hendraanggrian:packaging-gradle-plugin:$version")
    }
}

apply plugin: 'com.hendraanggrian.packaging'
```

## Usage

Below are example configuration for `Windows64` and `MacOS` distributions.
Note that properties of distribution configuration may override extension configuration.

```gradle
packaging {
    executable.set('example')
    classpath.set(new File('path/to/jar'))
    mainClass.set('com.example.App')
    vmArgs.addAll('-Xmx1G')
    resources.addAll(new File('image.jpg'), new File('path/to/other.jpg'))
    minimizeJre.set('hard')
    outputDirectory.set(new File('my/folder'))
    verbose.set(true)
    autoOpen.set(true)
}

tasks {
    packWindows64 {
        executable.set('example64')
        vmArgs.add('-Xdebug')
        appName.set('Example Windows 64-bit')
        jdk.set('path/to/windows_64_jdk')
    }
    packMacOS {
        name.set('Example.app')
        jdk.set('path/to/mac_jdk')
        icon.set(new File('path/to/mac_icon.icns'))
        bundleId.set('com.example.app')
    }
}
```

### Using [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

For easier setup, also use `application` plugin to distribute classpath with `installDist` command.

```gradle
apply plugin: 'application'
apply plugin: 'com.hendraanggrian.packaging'

application {
    applicationName = 'My App'
    mainClass.set('com.example.App')
}
```
