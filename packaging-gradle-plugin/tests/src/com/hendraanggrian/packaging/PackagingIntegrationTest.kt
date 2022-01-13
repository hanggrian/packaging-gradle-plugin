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

/**
 * Integration test are migrated here until this is fixed:
 * https://stackoverflow.com/questions/44679007/plugin-under-test-metadata-properties-not-created-by-gradle-testkit-when-running
 */
class PackagingIntegrationTest {

    @Rule @JvmField val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        testProjectDir.newFile("settings.gradle.kts").writeText(
            """
            rootProject.name = "integration-test"
            """.trimIndent()
        )
        buildFile = testProjectDir.newFile("build.gradle.kts")
        runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
    }

    @Test
    fun withApplicationPlugin() {
        buildFile.writeText(
            """
            plugins {
                application
                id("com.hendraanggrian.packaging")
            }
            application {
                applicationName = "MyApp"
                mainClass.set("com.example.App")
            }
            """.trimIndent()
        )
        runner.withArguments("build").build().let {
            assertEquals(TaskOutcome.SUCCESS, it.task(":build")!!.outcome)
        }
    }
}