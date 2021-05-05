package com.hendraanggrian.packaging

import com.badlogicgames.packr.PackrConfig
import com.hendraanggrian.packaging.internal.DefaultPackagingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

/** Plugin that creates native bundles for your JAR. */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "packaging"
        const val GROUP = PLUGIN_NAME

        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    override fun apply(project: Project) {
        val hasJavaPlugin = project.pluginManager.hasPlugin("java")
        val hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
        val extension = project.extensions.create(
            PackagingExtension::class, "packaging",
            DefaultPackagingExtension::class, project
        )
        project.afterEvaluate {
            if (hasJavaPlugin) {
                val sourceSets = project.extensions.getByType<SourceSetContainer>()
                extension.resources.set(sourceSets["main"].resources.srcDirs.filter { it.exists() })
            }
            if (hasApplicationPlugin) {
                val application = project.extensions.getByType<JavaApplication>()
                extension.appName.set(application.applicationName)
                extension.classpath.set(project.layout.buildDirectory.dir("install/${application.applicationName}/lib"))
                extension.mainClass.set(application.mainClass)
            }
        }
        PackrConfig.Platform.values().map {
            project.tasks.register<PackTask>("pack$it") {
                if (hasApplicationPlugin) {
                    dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
                }
                group = GROUP
                description = "Pack native bundles for $it."

                platform.set(it)
                verbose.set(extension.verbose)
                autoOpen.set(extension.autoOpen)
                jdk.set(System.getenv("JAVA_HOME") ?: System.getProperty("java.home"))

                appName.set(extension.appName)
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
            }
        }
    }
}
