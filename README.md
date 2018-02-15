Packr
=====
JavaFX app that wraps JARs into native bundle for Windows, MacOS, and Linux.
Basically GUI version of amazing [packr] library.

![demo][demo]

Download
--------
Head to [releases] to download this app in following flavors:
 * Executable jar file, requires JRE 1.8
 * MacOS app
 * Windows 64-bit exe

How to build
------------
Use `shadowJar` to build a fat jar which then should be packed either with `packr.jar` or with this app itself.

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
    
[demo]: /art/demo.png
[releases]: https://github.com/hendraanggrian/packr-gui/releases
[packr]: https://github.com/libgdx/packr