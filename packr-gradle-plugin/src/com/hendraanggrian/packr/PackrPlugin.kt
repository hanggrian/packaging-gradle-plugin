package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

/** Plugin that creates native distributions for your JAR. */
class PackrPlugin : Plugin<Project> {

    companion object {
        const val GROUP_NAME = "packr"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<PackrExtension>(GROUP_NAME, project.name, project.projectDir)
        ext.outputDir = project.buildDir.resolve("releases")

        project.afterEvaluate {
            if (ext.executable == null) {
                ext.executable = name
            }

            val packTaskProviders = ext.distributions.map {
                tasks.register<PackTask>("pack${it.platform.name}") {
                    group = GROUP_NAME
                    description = "Pack JARs in $it executable."
                    distribution = it
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

            tasks.register("packAll") {
                description = "Pack JARs for all configured platforms."
                group = GROUP_NAME
                dependsOn(*packTaskProviders.map { it.get() }.toTypedArray())
            }
        }
    }
}
