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
        classpath 'com.hendraanggrian:packr:0.2'
    }
}
```

then apply it in your module:

```gradle
apply plugin: 'packr'
```

Usage
-----
Configure `packr` extension, below are available configurations.
See [PackrConfiguration] for meaning of each parameter.

```gradle
packr {
    jdk 'path/to/jdk'                   // path to jdk directory of zip file
    
    executable 'example'                // default is project's name
    classpath('my.jar', 'other.jar')    // default is empty
    mainClass 'com.example.App'         // must be defined or will throw an exception
    vmArgs('Xmx1G')                     // default is empty
    resources('resources', 'image.jpg') // default is empty
    minimizeJre 'hard'                  // default is `soft`
    outputName 'Example App'            // default is project's name
    outputDir 'packr-output'            // default is `release` directory in build directoy
    
    iconDir 'icon.icns'                 // optional mac icon
    bundleId 'com.example.app'          // optional mac bundle
    
    openOnDone true                     // default is false
}
```

`packr` tasks will be created for each platform's jdk defined. You can then pack with each task:
```gradle
./gradlew packMacOS
./gradlew packWindows32
./gradlew packWindows64
./gradlew packLinux32
./gradlew packLinux64
```

Multiple platforms
------------------
Sometimes configurations are different on specific platforms (most likely `jdk` and `outputName`).
To override `packr` extension configurations, modify each tasks:

```gradle
tasks.getByName('packMacOS') {
    jdk 'my/jdk/path'
    outputName 'Mac App'
}

tasks.getByName('packWindows64') {
    vmArgs('-SomeWindowsArgument')
    outputDir 'somewhere/else'
}
```  

These tasks have the same properties as `packr` extension.

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
[PackrConfiguration]: https://hendraanggrian.github.io/packr-plugin/packr/com.hendraanggrian.packr/-packr-configuration/index.html