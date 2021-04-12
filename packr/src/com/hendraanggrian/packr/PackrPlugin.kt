package com.hendraanggrian.packr

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

/** Plugin that creates native bundles for your JAR. */
class PackrPlugin : Plugin<Project> {

    companion object {
        // Also a group name of Gradle Distribution Plugin.
        const val GROUP_NAME = "distribution"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<PackrExtension>("packr", project)

        project.afterEvaluate {
            val packTasks = ext.platformConfigurations.map { platformConfiguration ->
                tasks.register<PackTask>("pack$platformConfiguration") {
                    group = GROUP_NAME
                    description = "Pack native bundles for $platformConfiguration."
                    platform.set(platformConfiguration.platform)

                    releaseName.set(platformConfiguration.releaseName)
                    jdk.set(platformConfiguration.jdk)
                    icon.set(platformConfiguration.icon)
                    bundleId.set(platformConfiguration.bundleId)

                    executable.set(ext.executable)
                    classpath.set(ext.classpath)
                    removePlatformLibraries.set(ext.removePlatformLibraries)
                    mainClass.set(ext.mainClass)
                    vmArgs.addAll(ext.vmArgs)
                    resources.set(ext.resources)
                    minimizeJre.set(ext.minimizeJre)
                    outputDirectory.set(ext.outputDirectory)
                    cacheJreDirectory.set(ext.cacheJreDirectory)
                    verbose.set(ext.verbose)
                    autoOpen.set(ext.autoOpen)
                }
            }
            when {
                ext.platformConfigurations.isEmpty() -> logger.info("Packr configuration not found.")
                else -> {
                    logger.info("Packr configuration found for ${ext.platformConfigurations.joinToString()}.")
                    tasks.register("packAll") {
                        description = "Pack native bundles for all configured distributions."
                        group = GROUP_NAME
                        setDependsOn(packTasks)
                    }
                }
            }
        }
    }
}
