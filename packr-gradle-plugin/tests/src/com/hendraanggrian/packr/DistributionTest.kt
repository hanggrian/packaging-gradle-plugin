package com.hendraanggrian.packr

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class DistributionTest {

    @Test fun distribution() {
        val distribution = Distribution(Platform.Linux32, "MyApp", "/my/path/to/jdk")
        assertEquals(Platform.Linux32, distribution.platform)
        assertEquals("MyApp", distribution.name)
        assertEquals("/my/path/to/jdk", distribution.jdk)
    }

    @Test fun macOSDistribution() {
        val distribution = MacOSDistribution(File("/path/to/project"), "MyApp.app", "/my/path/to/jdk")
        distribution.icon("my/icon")
        distribution.bundleId = "my.app"
        assertEquals(Platform.MacOS, distribution.platform)
        assertEquals("MyApp.app", distribution.name)
        assertEquals("/my/path/to/jdk", distribution.jdk)
        assertEquals(File("/path/to/project/my/icon"), distribution.icon)
        assertEquals("my.app", distribution.bundleId)
    }
}