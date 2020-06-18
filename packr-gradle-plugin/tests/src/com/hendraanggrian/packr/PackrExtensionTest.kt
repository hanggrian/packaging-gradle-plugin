package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import com.google.common.truth.Truth.assertThat
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class PackrExtensionTest {
    private val fakeDir = File("/my/path")
    private val extension = PackrExtension("awesome", fakeDir)

    @Test fun relativePaths() {
        val assertIterable: (Iterable<File>) -> Unit = {
            assertThat(it).containsExactly(
                fakeDir.resolve("A"),
                fakeDir.resolve("B"),
                fakeDir.resolve("C")
            )
        }
        extension.classpath("A", "B", "C")
        extension.removePlatformLibs("A", "B", "C")
        extension.resources("A", "B", "C")
        extension.outputDirectory = "A"
        extension.cacheJreDirectory = "B"
        assertIterable(extension.classpath)
        assertIterable(extension.removePlatformLibs)
        assertIterable(extension.resources)
        assertEquals(fakeDir.resolve("A"), extension.outputDir)
        assertEquals(fakeDir.resolve("B"), extension.cacheJreDir)
    }

    @Test fun configureDistributions() {
        extension.windows32 { name = "Windows App" }
        extension.macOS { name = "Mac App" }
        assertEquals(
            "Windows App",
            extension.distributions.single { it.platform == PackrConfig.Platform.Windows32 }.name
        )
        assertEquals(
            "Mac App",
            extension.distributions.single { it.platform == PackrConfig.Platform.MacOS }.name
        )
    }
}