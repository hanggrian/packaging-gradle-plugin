package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

/** When [PackrPlugin] is applied, several instances of this [PackTask] with different platform name will be created. */
class PackrPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.run {
            val extension = extensions.create(EXTENSION_NAME, PackrExtension::class.java, this)
            tasks {
                Platform.values().forEach {
                    val task = "pack$it"(PackTask::class) {
                        group = EXTENSION_NAME
                        platform(it)
                    }
                    afterEvaluate { extension applyTo task }
                }
            }
        }
    }

    private companion object {
        const val EXTENSION_NAME = "packr"
    }
}