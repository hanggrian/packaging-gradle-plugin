package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.closureOf

class PackrPlugin : Plugin<Project> {

    private lateinit var project: Project

    override fun apply(target: Project) {
        project = target
        val extension = project.extensions.create(EXTENSION_NAME, PackrExtension::class.java, project)
        project.afterEvaluate { extension.toConfigs().forEach { createPackTask(it) } }
    }

    private fun createPackTask(configuration: PackrConfig) = project.task(
        mapOf("type" to PackTask::class.java),
        "pack${configuration.platform}",
        closureOf<PackTask> {
            group = EXTENSION_NAME
            config = configuration
        })

    companion object {
        private const val EXTENSION_NAME = "packr"
    }
}