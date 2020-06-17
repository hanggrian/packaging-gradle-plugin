[![download](https://api.bintray.com/packages/hendraanggrian/maven/packr-gradle-plugin/images/download.svg)](https://bintray.com/hendraanggrian/maven/packr-gradle-plugin/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/packr-gradle-plugin.svg)](https://travis-ci.com/hendraanggrian/packr-gradle-plugin)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![license](https://img.shields.io/github/license/hendraanggrian/packr-gradle-plugin)](http://www.apache.org/licenses/LICENSE-2.0)

Packr Gradle Plugin
===================
Gradle plugin of [packr], a library that wraps JARs into native bundle for Windows, macOS, and Linux.
* Complete customization for each distribution.
* Pack multiple distributions with a single task.

Download
--------
```gradle
buildscript {
    repositories {
        jcenter()
        maven { url = 'https://oss.sonatype.org/content/repositories/snapshots/' }
    }
    dependencies {
        classpath 'com.hendraanggrian:packr-gradle-plugin:$version'
    }
}
```

Usage
-----
Below are example configuration for `Windows64` and `MacOS` distributions.
Note that properties of distribution configuration may override extension configuration.

```gradle
apply plugin: 'com.hendraanggrian.packr'

packr {
    executable 'example'
    classpath 'my.jar', 'path/to/other.jar'
    mainClass 'com.example.App'
    vmArgs '-Xmx1G'
    resources 'image.jpg', 'path/to/other.jpg'
    minimizeJre 'hard'
    outputDirectory 'my/folder'   
    isVerbose true
    isAutoOpen true
    
    configureWindows64 {
        executable 'example64' // overriding `example`
        vmArgs.add('-Xdebug') // arguments added after `-Xmx1G`
        
        // distribution-specific properties
        name = 'Example Windows 64-bit'
        jdk = 'path/to/windows_64_jdk'
    }
    
    configureMacOS {
        // distribution-specific properties
        name = 'Example.app'
        jdk = 'path/to/mac_jdk'
        
        // mac-only properties
        icon = 'path/to/mac_icon.icns'
        bundleId = 'com.example.app'
    }
}
```

After project evaluation, packr will then register task `packMacOS`, `packWindows64`, and also `packAll`.

### Default configuration
Packr extension and distributions' default configuration can be illustrated as below.

```gradle
packr {
    executable = project.name
    classpath = []
    resources = []
    minimizeJre = 'soft'
    outputDirectory = 'build/releases'
    vmArgs = []
    isVerbose = false
    isAutoOpen = false
    
    configureWindows32, configureWindows64, configureLinux32, configureLinux64 {
        name = project.name
        jdk = JDK_HOME
    }
    configureOS {
        name = project.name
        jdk = JDK_HOME
        icon = null
        bundleId = project.group
    }
}
```

### Using [Distribution Plugin]
For easier setup, also use `distribution` plugin to distribute classpath with `installDist` command.

```gradle
apply plugin: 'distributions'

afterEvaluate {
    tasks.getByName('packAll') {
        dependsOn 'installDist'
        mustRunAfter 'installDist'
    }
}
```
    
[packr]: https://github.com/libgdx/packr
[Distribution Plugin]: https://docs.gradle.org/current/userguide/distribution_plugin.html