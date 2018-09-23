package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class PackrPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks {
            val pack = register("pack", PackTask::class) {
                group = "packr"
            }
            project.afterEvaluate {
                pack {
                    if (executable == null) executable = project.name
                    if (outputDir == null) outputDir = "${project.buildDir}/release"
                }
            }
        }
    }
}