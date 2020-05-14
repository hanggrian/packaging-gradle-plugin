package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class PackrPlugin : Plugin<Project> {

    companion object {
        const val GROUP_NAME = "packr"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<PackrExtension>(GROUP_NAME, project.name, project.projectDir)
        ext.outputDir = project.buildDir.resolve("releases")

        lateinit var packTaskProviders: Iterable<TaskProvider<PackTask>>
        packTaskProviders = Platform.values().map {
            project.tasks.register<PackTask>("pack$it") {
                group = GROUP_NAME
                description = "Pack JARs in $it executable."
                platform = it
            }
        }
        project.tasks.register("packAll") {
            description = "Pack JARs for all configured platforms."
            group = GROUP_NAME
            dependsOn(*packTaskProviders.map { it.get() }.toTypedArray())
        }

        project.afterEvaluate {
            if (ext.executable == null) {
                ext.executable = project.name
            }
            packTaskProviders.forEach {
                it {
                    distribution = ext[platform]
                    executable = ext.executable
                    classpath = ext.classpath
                    removePlatformLibs = ext.removePlatformLibs
                    mainClass = ext.mainClass
                    vmArgs = ext.vmArgs
                    resources = ext.resources
                    minimizeJre = ext.minimizeJre
                    outputDir = ext.outputDir
                    cacheJreDir = ext.cacheJreDir
                    isVerbose = ext.isVerbose
                    isAutoOpen = ext.isAutoOpen
                }
            }
        }
    }
}
