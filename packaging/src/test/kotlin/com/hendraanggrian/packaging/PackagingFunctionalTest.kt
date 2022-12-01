package com.hendraanggrian.packaging

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class PackagingFunctionalTest {
    @Rule @JvmField
    val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        testProjectDir.newFile("settings.gradle.kts").writeText(
            """
            rootProject.name = "functional-test"
            """.trimIndent()
        )
        buildFile = testProjectDir.newFile("build.gradle.kts")
        runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
    }

    // TODO: fix NullPointerException
    // @Test
    fun minimalConfiguration() {
        testProjectDir.newFolder("lib").resolve("sample.jar").createNewFile()
        buildFile.writeText(
            """
            plugins {
                id("com.hendraanggrian.packaging")
            }
            packaging {
                mainJar.set("sample.jar")
                mainClass.set("com.example.App")
                inputDirectory.set(projectDir.resolve("lib"))
            }
            """.trimIndent()
        )
        assertEquals(
            TaskOutcome.UP_TO_DATE,
            runner.withArguments("check").build().task(":check")!!.outcome
        )
    }
}
