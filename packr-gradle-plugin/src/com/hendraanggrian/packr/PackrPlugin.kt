@file:Suppress("UnusedImport")

package com.hendraanggrian.packr

import com.badlogicgames.packr.PackrConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate // ktlint-disable

@Suppress("unused")
class PackrPlugin : Plugin<Project> {

    companion object {
        const val GROUP_NAME = "packr"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create<PackrExtension>(GROUP_NAME)
        ext.outputDirectory = "${project.buildDir}/releases"
        project.tasks {
            val packWindows32 by registering(PackTask::class) {
                group = GROUP_NAME
                extension = ext
                platform = PackrConfig.Platform.Windows32
            }
            val packWindows64 by registering(PackTask::class) {
                group = GROUP_NAME
                extension = ext
                platform = PackrConfig.Platform.Windows64
            }
            val packLinux32 by registering(PackTask::class) {
                group = GROUP_NAME
                extension = ext
                platform = PackrConfig.Platform.Linux32
            }
            val packLinux64 by registering(PackTask::class) {
                group = GROUP_NAME
                extension = ext
                platform = PackrConfig.Platform.Linux64
            }
            val packMacOS by registering(PackTask::class) {
                group = GROUP_NAME
                extension = ext
                platform = PackrConfig.Platform.MacOS
            }
            @Suppress("UnstableApiUsage")
            register("packAll") {
                group = GROUP_NAME
                dependsOn(
                    packWindows32.get(),
                    packWindows64.get(),
                    packLinux32.get(),
                    packLinux64.get(),
                    packMacOS.get()
                )
            }
        }
        project.afterEvaluate {
            if (ext.executable == null) {
                ext.executable = project.name
            }
        }
    }
}