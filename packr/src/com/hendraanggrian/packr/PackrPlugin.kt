package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

class PackrPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, PackrExtension::class.java, project)
        project.run {
            tasks {
                PackrConfig.Platform.values().forEach {
                    val packTask = "pack$it"(PackTask::class) {
                        group = EXTENSION_NAME
                        setPlatform(it)
                    }
                    afterEvaluate { extension applyTo packTask }
                }
            }
        }
    }

    private companion object {
        const val EXTENSION_NAME = "packr"
    }
}