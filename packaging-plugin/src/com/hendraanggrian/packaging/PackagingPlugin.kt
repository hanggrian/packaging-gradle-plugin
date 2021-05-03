package com.hendraanggrian.packaging

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.Distribution
import org.gradle.api.distribution.DistributionContainer
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

/** Plugin that creates native bundles for your JAR. */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "packaging"
        const val GROUP = PLUGIN_NAME
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(ApplicationPlugin::class)

        val application = project.extensions.getByName(ApplicationPlugin.APPLICATION_PLUGIN_NAME) as JavaApplication
        val distribution: Distribution = (project.extensions.getByName("distributions") as DistributionContainer)
            .getByName(DistributionPlugin.MAIN_DISTRIBUTION_NAME)

        val extension = project.extensions.create<PackagingExtension>("packaging", project, application, distribution)
        project.afterEvaluate {
            val packTasks = extension.platformConfigurations.map { platformConfiguration ->
                tasks.register<PackTask>("pack$platformConfiguration") {
                    dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
                    group = GROUP
                    description = "Pack native bundles for $platformConfiguration."
                    platform.set(platformConfiguration.platform)

                    releaseName.set(platformConfiguration.releaseName)
                    jdk.set(platformConfiguration.jdk)
                    icon.set(platformConfiguration.icon)
                    bundleId.set(platformConfiguration.bundleId)

                    executable.set(extension.executable)
                    classpath.set(extension.classpath)
                    removePlatformLibraries.set(extension.removePlatformLibraries)
                    mainClass.set(extension.mainClass)
                    vmArgs.addAll(extension.vmArgs)
                    resources.set(extension.resources)
                    minimizeJre.set(extension.minimizeJre)
                    outputDirectory.set(extension.outputDirectory)
                    cacheJreDirectory.set(extension.cacheJreDirectory)
                    verbose.set(extension.verbose)
                    autoOpen.set(extension.autoOpen)
                }
            }
            when {
                extension.platformConfigurations.isEmpty() -> logger.info("Packr configuration not found.")
                else -> {
                    logger.info("Packr configuration found for ${extension.platformConfigurations.joinToString()}.")
                    tasks.register("packAll") {
                        description = "Pack native bundles for all configured distributions."
                        group = GROUP
                        setDependsOn(packTasks)
                    }
                }
            }
        }
    }
}
