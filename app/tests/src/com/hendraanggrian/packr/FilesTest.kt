package com.hendraanggrian.packr

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(JUnitPlatform::class)
object FilesTest : Spek({

    given("A file and a json file") {
        val file = File("a/b/c", "hello.txt")
        val jsonFile = File("a/b/c", "my.json")
        it("file is relative to directory") {
            assertTrue(file relativeTo jsonFile)
        }
        it("returns relative file name") {
            assertEquals(file - jsonFile, "hello.txt")
        }
    }
})