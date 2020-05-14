[![download](https://api.bintray.com/packages/hendraanggrian/maven/packr-gradle-plugin/images/download.svg)](https://bintray.com/hendraanggrian/maven/packr-gradle-plugin/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/packr-gradle-plugin.svg)](https://travis-ci.com/hendraanggrian/packr-gradle-plugin)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![license](https://img.shields.io/github/license/hendraanggrian/packr-gradle-plugin)](http://www.apache.org/licenses/LICENSE-2.0)

Packr Gradle Plugin
===================
Gradle plugin of [packr], a library that wraps JARs into native bundle for Windows, macOS, and Linux.
* Pack multiple bundles with single command.
* For easier setup, also use `distribution` plugin to distribute classpath with `installDist` command.

Download
--------
```gradle
buildscript {
    repositories {
        jcenter()
        maven { url = 'https://oss.sonatype.org/content/repositories/snapshots/' }
    }
    dependencies {
        classpath "com.hendraanggrian:packr-gradle-plugin:$version"
    }
}
```

Usage
-----
Configure `packr` task, below are available configurations.

```gradle
apply plugin: 'com.hendraanggrian.packr'

packr {
    executable 'example'
    classpath 'my.jar', 'path/to/other.jar'
    mainClass 'com.example.App'
    vmArgs 'Xmx1G'
    resources 'image.jpg', 'path/to/other.jpg'
    minimizeJre 'hard'
    outputDirectory 'my/folder'   
    isVerbose true
    isAutoOpen true
    
    configureMacOS {
        name 'Example.app'
        jdk 'path/to/mac_jdk'
        icon 'path/to/mac_icon.icns'
        bundleId 'com.example.app'
        vmArgs '-Xmx512M'
    }
    configureWindows32 {
        name 'Example Windows 32-bit'
        jdk 'path/to/windows_32_jdk'
        vmArgs '-Xmx256M'
    }
    configureWindows64 {
        name 'Example Windows 64-bit'
        jdk 'path/to/windows_64_jdk'
        vmArgs '-Xmx512M'
    }
    configureLinux32 {
        name 'Example Windows 32-bit'
        jdk 'path/to/windows_32_jdk'
        vmArgs '-Xmx256M'
    }
    configureLinux64 {
        name 'Example Linux 64-bit'
        jdk 'path/to/linux_64_jdk'
        vmArgs '-Xmx512M'
    }
}
```

Packr will then register task to each distribution (e.g.: `packMacOS`, `packWindows32`, etc.).
Each of those task will only take effect if related distribution is configured.

### Default configuration

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
}
```
    
[packr]: https://github.com/libgdx/packr
[PackrTask]: https://hendraanggrian.github.io/packr-plugin/packr/com.hendraanggrian.packr/-packr-task/index.html

packr-task/index.html
ckr/-packr-task/index.html
