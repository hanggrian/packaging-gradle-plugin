package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig.Platform
import com.badlogicgames.packr.PackrConfig.Platform.Linux32
import com.badlogicgames.packr.PackrConfig.Platform.Linux64
import com.badlogicgames.packr.PackrConfig.Platform.MacOS
import com.badlogicgames.packr.PackrConfig.Platform.Windows32
import com.badlogicgames.packr.PackrConfig.Platform.Windows64
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.closureOf

class PackrPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.createTask(MacOS)
        project.createTask(Windows32)
        project.createTask(Windows64)
        project.createTask(Linux32)
        project.createTask(Linux64)
    }

    private fun Project.createTask(platform: Platform) = task(
        mapOf("type" to PackTask::class.java, "group" to "packr"),
        "pack$platform",
        closureOf<PackTask> { setPlatform(platform) })
}