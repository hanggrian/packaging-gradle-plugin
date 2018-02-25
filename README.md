Packr Plugin
============
Gradle plugin of [packr] which wraps JARs into native bundle for Windows, MacOS, and Linux.

Download
--------
Add plugin to buildscript:

```gradle
buildscript {
    repositories {
        jcenter()
        maven { url = 'https://oss.sonatype.org/content/repositories/snapshots' }
    }
    dependencies {
        classpath 'com.hendraanggrian:packr:0.1'
    }
}
```

then apply it in your module:

```gradle
apply plugin: 'packr'
```

Usage
-----
Configure `packr` extension, below are all possible configurations:

```gradle
packr {
    // define jdk to each platform, or ignore any platform you want to leave behind
    platforms.mac = 'path/to/jdk'
    platforms.windows32 = 'path/to/jdk'
    platforms.windows64 = 'path/to/jdk'
    platforms.linux32 = 'path/to/jdk'
    platforms.linux64 = 'path/to/jdk'
    
    executable 'example'                // default is project's name
    classpath('my.jar', 'other.jar')    // default is empty
    mainClass 'com.example.App'         // default is empty
    vmArgs('Xmx1G')                     // default is empty
    resources('resources', 'image.jpg') // default is empty
    minimizeJRE 'hard'                  // default is `soft`
    outputName 'Example'                // default is project's name
    outputDirectory 'packr-output'      // default is `release` directory in build directoy
    
    icon 'icon.icns'                    // optional mac icon
    bundle 'com.example.app'            // optional mac bundle
}
```

`packr` tasks will be created for each platform's jdk defined. You can then pack with task:
```gradle
./gradlew packMacOS
./gradlew packWindows32
./gradlew packWindows64
./gradlew packLinux32
./gradlew packLinux64
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