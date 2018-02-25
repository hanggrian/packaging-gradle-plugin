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
            jdk = _jdks.singleOrNull { java.io.File(it).exists() } ?: error("No JDK available")
            executable = _executable ?: project.name
            classpath = _classpath.map { File(project.projectDir, it).path }
            mainClass = _mainClass ?: error("Undefined main class")
            vmArgs = _vmArgs
            resources = _resources.map { File(project.projectDir, it) }
            minimizeJre = _minimizeJre ?: "soft"
            outputDir = project.buildDir.resolve(_outputDir ?: "release")
            macWrapApp = _macWrapApp
            macIcon = _macIcon?.let { File(project.projectDir, it) } ?: Unit
            macBundle = _macBundle ?: Unit
        })

    companion object {
        internal const val EXTENSION_NAME = "packr"
    }
}