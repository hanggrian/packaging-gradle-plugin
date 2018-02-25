package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.closureOf
import java.io.File

class PackrPlugin : Plugin<Project> {

    private lateinit var project: Project

    override fun apply(target: Project) {
        project = target
        val extension = project.extensions.create(EXTENSION_NAME, PackrExtension::class.java)
        project.afterEvaluate { extension.createPackTask() }
    }

    private fun PackrExtension.createPackTask() = project.task(
        mapOf("type" to PackTask::class.java),
        "pack",
        closureOf<PackTask> {
            macJdk = platform.mac ?: ""
            windows32Jdk = platform.windows32 ?: ""
            windows64Jdk = platform.windows64 ?: ""
            linux32Jdk = platform.linux32 ?: ""
            linux64Jdk = platform.linux64 ?: ""

            executable = _executable ?: project.name
            classpath = _classpath.map { File(project.projectDir, it).path }
            mainClass = _mainClass ?: error("Undefined main class")
            vmArgs = _vmArgs
            resources = _resources.map { File(project.projectDir, it) }
            minimizeJre = _minimizeJre ?: "soft"
            outputDir = project.buildDir.resolve(_outputDir ?: "release").resolve(executable)

            macWrapApp = _macWrapApp
            macIcon = _macIcon?.let { File(project.projectDir, it).path } ?: ""
            macBundle = _macBundle ?: ""
        })

    companion object {
        internal const val EXTENSION_NAME = "packr"
    }
}