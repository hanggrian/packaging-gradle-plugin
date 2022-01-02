package com.hendraanggrian.packaging

import com.hendraanggrian.packaging.internal.AbstractPackTask
import com.hendraanggrian.packaging.internal.DefaultPackagingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/** Plugin that creates native bundles for your JAR. */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "packaging"
        const val GROUP_NAME = PLUGIN_NAME
    }

    private var hasJavaPlugin: Boolean = false
    private var hasApplicationPlugin: Boolean = false
    private lateinit var extension: PackagingExtension

    override fun apply(project: Project) {
        hasJavaPlugin = project.pluginManager.hasPlugin("java") || project.pluginManager.hasPlugin("java-library")
        hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
        extension = project.extensions.create(
            PackagingExtension::class, "packaging",
            DefaultPackagingExtension::class, project
        )

        val packWindows by project.tasks.registering(PackWindowsTask::class) { setup("Windows") }
        val packLinux by project.tasks.registering(PackLinuxTask::class) { setup("Linux") }
        val packMacOS by project.tasks.registering(PackMacOSTask::class) { setup("macOS") }

        project.afterEvaluate {
            extension.appVersion.convention(project.version.toString())
            extension.appName.convention(project.name)
            /*if (hasJavaPlugin) {
                val sourceSets = project.extensions.getByName<SourceSetContainer>("sourceSets")
                extension.resourcesDirectory.convention(sourceSets["main"].resources.srcDirs.filter { it.exists() })
            }*/
            if (hasApplicationPlugin) {
                val application = project.extensions
                    .getByName<JavaApplication>(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
                extension.appName.convention(application.applicationName)
                extension.inputDirectory.convention(
                    project.layout.buildDirectory.dir("install/${application.applicationName}/lib")
                )
                extension.mainClass.convention(application.mainClass)
            }
        }
    }

    private fun AbstractPackTask.setup(desc: String) {
        if (hasApplicationPlugin) {
            dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
        }
        group = GROUP_NAME
        description = "Pack native bundles for $desc."

        verbose.set(extension.verbose)
        appVersion.set(extension.appVersion)
        copyright.set(extension.copyright)
        appDescription.set(extension.appDescription)
        appName.set(extension.appName)
        outputDirectory.set(extension.outputDirectory)
        addModules.set(extension.addModules)
        modulePath.set(extension.modulePath)
        bindServices.set(extension.bindServices)
        runtimeImage.set(extension.runtimeImage)
        icon.set(extension.icon)
        inputDirectory.set(extension.inputDirectory)
        addLauncher.set(extension.addLauncher)
        arguments.set(extension.arguments)
        javaOptions.set(extension.javaOptions)
        mainClass.set(extension.mainClass)
        mainJar.set(extension.mainJar)
        module.set(extension.module)
        appImage.set(extension.appImage)
        fileAssociations.set(extension.fileAssociations)
        installDirectory.set(extension.installDirectory)
        licenseFile.set(extension.licenseFile)
        resourcesDirectory.set(extension.resourcesDirectory)
    }
}
