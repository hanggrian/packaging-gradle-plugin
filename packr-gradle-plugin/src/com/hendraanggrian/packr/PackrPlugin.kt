package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class PackrPlugin : Plugin<Project> {

    private companion object {
        const val TASK_NAME = "pack"
        const val GROUP_NAME = "packr"
    }

    override fun apply(project: Project) {
        project.tasks {
            val pack = register(TASK_NAME, PackTask::class) {
                group = GROUP_NAME
                outputDir = project.buildDir.resolve("release")
            }
            project.afterEvaluate {
                pack {
                    if (executable.isEmpty()) executable = project.name
                }
            }
        }
    }
}