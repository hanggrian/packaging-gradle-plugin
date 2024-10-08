package com.hanggrian.packaging

import org.gradle.language.base.plugins.LifecycleBasePlugin.CHECK_TASK_NAME
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
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
    @Rule @JvmField
    val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File
    private lateinit var runner: GradleRunner

    @BeforeTest
    @Throws(IOException::class)
    fun setup() {
        testProjectDir.newFile("settings.gradle.kts").writeText(
            """
            rootProject.name = "integration-test"
            """.trimIndent(),
        )
        buildFile = testProjectDir.newFile("build.gradle.kts")
        runner =
            GradleRunner
                .create()
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
                id("com.hanggrian.packaging")
            }
            application {
                applicationName = "MyApp"
                mainClass.set("com.example.App")
            }
            """.trimIndent(),
        )
        assertEquals(
            UP_TO_DATE,
            runner.withArguments(CHECK_TASK_NAME).build().task(":$CHECK_TASK_NAME")!!.outcome,
        )
    }
}
