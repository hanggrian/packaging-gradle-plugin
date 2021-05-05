package com.hendraanggrian.packaging

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PackagingFunctionalTest {
    @Rule @JvmField val testProjectDir = TemporaryFolder()
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        settingsFile = testProjectDir.newFile("settings.gradle.kts")
        settingsFile.writeText(
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

    @Test
    fun minimalConfiguration() {
        testProjectDir.newFolder("lib").resolve("sample.jar").createNewFile()
        buildFile.writeText(
            """
            plugins {
                id("com.hendraanggrian.packaging")
            }
            packaging {
                mainClass.set("com.example.App")
                classpath.set(projectDir.resolve("lib"))
            }
            """.trimIndent()
        )
        runner.withArguments("packMacOS").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":packMacOS")!!.outcome)
            assertTrue(
                testProjectDir.root.resolve("build/install/MacOS")
                    .resolve("functional-test")
                    .exists()
            )
        }
    }

    // @Test
    fun configureSome() {
        buildFile.writeText(
            """
            plugins {
                id("com.hendraanggrian.packaging")
            }
            application {
                mainClass.set("com.example.App")
            }
            packaging {
                mainClass.set("com.example.App")
                releaseName.set("MyApp")
            }
            """.trimIndent()
        )
        runner.withArguments("packMacOS").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":packMacOS")!!.outcome)
            assertTrue(
                testProjectDir.root.resolve("build")
                    .resolve("MyApp")
                    .exists()
            )
        }
    }
}