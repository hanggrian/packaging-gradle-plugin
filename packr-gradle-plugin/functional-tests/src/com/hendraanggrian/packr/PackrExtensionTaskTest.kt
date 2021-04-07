package com.hendraanggrian.packr

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

class PackrExtensionTaskTest {

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
    fun noConfiguration() {
        File(javaClass.getResource("${File.separator}sample.jar").toURI())
            .copyTo(testProjectDir.newFolder("lib").resolve("sample.jar"))
        buildFile.writeText(
            """
            plugins {
                java
                idea
                id("com.hendraanggrian.packr")
            }
            packr {
                mainClass.set("com.example.App")
                classpath.add(projectDir.resolve("lib"))
                configureMacOS()
            }
            """.trimIndent()
        )
        runner.withArguments("packMacOS").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":packMacOS")!!.outcome)
            assertTrue(
                testProjectDir.root.resolve("build${File.separator}releases")
                    .resolve("functional-test-MacOS")
                    .exists()
            )
        }
    }

    @Test
    fun configureSome() {
        File(javaClass.getResource("${File.separator}sample.jar").toURI())
            .copyTo(testProjectDir.newFolder("lib").resolve("sample.jar"))
        buildFile.writeText(
            """
            plugins {
                java
                idea
                id("com.hendraanggrian.packr")
            }
            packr {
                mainClass.set("com.example.App")
                classpath.add(projectDir.resolve("lib"))
                outputDirectory.set(buildDir)
                macOS {
                    releaseName.set("MyApp")
                }
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