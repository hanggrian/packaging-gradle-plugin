package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.closureOf

class PackrPlugin : Plugin<Project> {

    private lateinit var project: Project

    override fun apply(target: Project) {
        project = target
        val extension = project.extensions.create(EXTENSION_NAME, PackrExtension::class.java, project)
        project.afterEvaluate {
            val configs = extension.toConfigs()
            Platform.values().forEach { createPackTask(it, configs) }
        }
    }

    private fun createPackTask(platform: Platform, configs: List<PackrConfig>) = project.task(
        mapOf("type" to PackTask::class.java),
        "pack$platform",
        closureOf<PackTask> {
            group = EXTENSION_NAME
            this.platform = platform
            this.configs = configs
        })

    companion object {
        private const val EXTENSION_NAME = "packr"
    }
}