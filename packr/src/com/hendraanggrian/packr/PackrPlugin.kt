package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

@Suppress("unused")
class PackrPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.run {
            tasks {
                "pack"(PackTask::class) {
                    group = "packr"
                    afterEvaluate {
                        if (executable == null) executable = project.name
                        if (outputDir == null) outputDir = project.buildDir.resolve("release")
                    }
                }
            }
        }
    }
}