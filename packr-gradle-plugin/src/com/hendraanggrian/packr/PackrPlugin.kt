package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class PackrPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.run {
            tasks {
                val pack = register("pack", PackTask::class) {
                    group = "packr"
                }
                afterEvaluate {
                    pack.get().let {
                        if (it.executable == null) it.executable = project.name
                        if (it.outputDir == null) it.outputDir = "${project.buildDir}/release"
                    }
                }
            }
        }
    }
}