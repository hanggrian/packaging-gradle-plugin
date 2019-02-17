Packr Plugin
============
[![bintray](https://img.shields.io/badge/bintray-maven-brightgreen.svg)](https://bintray.com/hendraanggrian/maven)
[![download](https://api.bintray.com/packages/hendraanggrian/packr/packr-gradle-plugin/images/download.svg)](https://bintray.com/hendraanggrian/packr/packr-gradle-plugin/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/packr-gradle-plugin.svg)](https://travis-ci.com/hendraanggrian/packr-gradle-plugin)
[![license](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Gradle plugin of [packr], a library that wraps JARs into native bundle for Windows, macOS, and Linux.

Download
--------
Add plugin to buildscript:

```gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.hendraanggrian.packr:packr-gradle-plugin:$version"
    }
}
```

Usage
-----
Apply plugin in your module.

```gradle
apply plugin: 'com.hendraanggrian.packr'
```

Configure `packr` task, below are available configurations.

```gradle
packr {
    executable 'example'
    classpath 'my.jar', 'path/to/other.jar'
    mainClass 'com.example.App'
    vmArgs 'Xmx1G'
    resources 'image.jpg', 'path/to/other.jpg'
    minimizeJre 'hard'
    outputDirectory 'my/folder'   
    verbose true
    openOnDone true
    
    macOS {
        name 'Example.app'
        jdk 'path/to/mac_jdk'
        icon 'path/to/mac_icon.icns'
        bundleId 'com.example.app'
        vmArgs '-Xmx512M'
    }
    windows32 {
        name 'Example Windows 32-bit'
        jdk 'path/to/windows_32_jdk'
        vmArgs '-Xmx256M'
    }
    windows64 {
        name 'Example Windows 64-bit'
        jdk 'path/to/windows_64_jdk'
        vmArgs '-Xmx512M'
    }
    linux32 {
        name 'Example Windows 32-bit'
        jdk 'path/to/windows_32_jdk'
        vmArgs '-Xmx256M'
    }
    linux64 {
        name 'Example Linux 64-bit'
        jdk 'path/to/linux_64_jdk'
        vmArgs '-Xmx512M'
    }
}
```

Packr will then register task to each distribution (e.g.: `packMacOS`, `packWindows32`, etc.).
Each of those task will only take effect if related distribution is configured.

#### Default configuration

```gradle
packr {
    executable = project.name
    classpath = []
    resources = []
    minimizeJre = PackrExtension.MINIMIZE_SOFT
    outputDirectory = 'build/releases'
    vmArgs = []
    verbose = false
    openOnDone = false
}
```

License
-------
    Copyright 2018 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[packr]: https://github.com/libgdx/packr
[PackrTask]: https://hendraanggrian.github.io/packr-plugin/packr/com.hendraanggrian.packr/-packr-task/index.html
