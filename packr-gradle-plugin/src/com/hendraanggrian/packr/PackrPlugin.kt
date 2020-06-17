package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

/** Plugin that creates native bundles for your JAR. */
class PackrPlugin : Plugin<Project> {

    companion object {
        const val GROUP_NAME = "packaging"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<PackrExtension>("packr", project.name, project.group, project.projectDir)
        ext.outputDir = project.buildDir.resolve("releases")

        project.afterEvaluate {
            val packTasks = ext.distributions.map {
                logger.info("Packr configuration found for $it.")
                tasks.register<PackTask>("pack${it.platform.name}") {
                    group = GROUP_NAME
                    description = "Pack native bundles for $it."
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

            if (packTasks.isNotEmpty()) {
                tasks.register("packAll") {
                    description = "Pack native bundles for all configured distributions."
                    group = GROUP_NAME
                    setDependsOn(packTasks)
                }
            }
        }
    }
}
