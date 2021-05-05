package com.hendraanggrian.packaging

import com.badlogicgames.packr.PackrConfig
import com.google.gradle.osdetector.OsDetector
import com.google.gradle.osdetector.OsDetectorPlugin
import com.hendraanggrian.packaging.internal.DefaultPackagingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/** Plugin that creates native bundles for your JAR. */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "packaging"
        const val GROUP_NAME = PLUGIN_NAME

        const val MINIMIZATION_SOFT = "soft"
        const val MINIMIZATION_HARD = "hard"
        const val MINIMIZATION_ORACLEJRE8 = "oraclejre8"
    }

    private var hasJavaPlugin: Boolean = false
    private var hasApplicationPlugin: Boolean = false
    private lateinit var extension: PackagingExtension

    override fun apply(project: Project) {
        project.pluginManager.apply(OsDetectorPlugin::class)
        hasJavaPlugin = project.pluginManager.hasPlugin("java")
        hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
        extension = project.extensions.create(
            PackagingExtension::class, "packaging",
            DefaultPackagingExtension::class, project
        )

        val packWindows32 by project.tasks.registering(PackTask::class) { setup(PackrConfig.Platform.Windows32) }
        val packWindows64 by project.tasks.registering(PackTask::class) { setup(PackrConfig.Platform.Windows64) }
        val packLinux32 by project.tasks.registering(PackTask::class) { setup(PackrConfig.Platform.Linux32) }
        val packLinux64 by project.tasks.registering(PackTask::class) { setup(PackrConfig.Platform.Linux64) }
        val packMacOS by project.tasks.registering(PackTask::class) { setup(PackrConfig.Platform.MacOS) }
        val packAll by project.tasks.registering {
            group = GROUP_NAME
            description = "Pack native bundles for all platforms with configured JDK."
        }

        project.afterEvaluate {
            val detector = project.extensions.getByType(OsDetector::class)
            when (detector.os) {
                "windows" -> when {
                    detector.arch.endsWith("32") -> packWindows32(::useJavaHome)
                    detector.arch.endsWith("64") -> packWindows64(::useJavaHome)
                }
                "linux" -> when {
                    detector.arch.endsWith("32") -> packLinux32(::useJavaHome)
                    detector.arch.endsWith("64") -> packLinux64(::useJavaHome)
                }
                "osx" -> packMacOS(::useJavaHome)
            }
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
            val availableTasks = listOf(packWindows32, packWindows64, packLinux32, packLinux64, packMacOS)
                .filter { it.get().jdk.isPresent }
            when {
                availableTasks.isNotEmpty() -> packAll { dependsOn(availableTasks) }
                else -> packAll { doFirst { error("No platforms with configured JDK") } }
            }
        }
    }

    private fun useJavaHome(task: PackTask) {
        if (!task.jdk.isPresent) {
            task.jdk.set(
                checkNotNull(System.getenv("JAVA_HOME") ?: System.getProperty("java.home")) {
                    "`JAVA_HOME` system environment not found"
                }
            )
        }
    }

    private fun PackTask.setup(target: PackrConfig.Platform) {
        if (hasApplicationPlugin) {
            dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
        }
        group = GROUP_NAME
        description = "Pack native bundles for $target."

        platform.set(target)
        verbose.set(extension.verbose)
        autoOpen.set(extension.autoOpen)

        appName.set(extension.appName)
        executable.set(extension.executable)
        classpath.set(extension.classpath)
        removePlatformLibraries.set(extension.removePlatformLibraries)
        mainClass.set(extension.mainClass)
        vmArgs.set(extension.vmArgs)
        resources.set(extension.resources)
        minimizeJre.set(extension.minimizeJre)
        outputDirectory.set(extension.outputDirectory)
        cacheJreDirectory.set(extension.cacheJreDirectory)
        verbose.set(extension.verbose)
    }
}
