package com.hendraanggrian.packaging

import com.google.gradle.osdetector.OsDetector
import com.google.gradle.osdetector.OsDetectorPlugin
import com.hendraanggrian.packaging.internal.DefaultPackagingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

/** Plugin that creates native bundles for your JAR. */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val PACKAGING_GROUP = "packaging"
        const val TASK_PACK_EXE_NAME = "packExe"
        const val TASK_PACK_MSI_NAME = "packMsi"
        const val TASK_PACK_DMG_NAME = "packDmg"
        const val TASK_PACK_PKG_NAME = "packPkg"
        const val TASK_PACK_DEB_NAME = "packDeb"
        const val TASK_PACK_RPM_NAME = "packRpm"
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(OsDetectorPlugin::class)
        val hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)

        val packagingExtension = project.extensions.create(
            PackagingExtension::class, "packaging", DefaultPackagingExtension::class, project
        )
        val detectorExtension = project.extensions.getByType<OsDetector>()
        val os = DefaultNativePlatform.getCurrentOperatingSystem()
        when {
            os.isWindows -> {
                createPackTask(project, TASK_PACK_EXE_NAME, hasApplicationPlugin)
                createPackTask(project, TASK_PACK_MSI_NAME, hasApplicationPlugin)
            }
            os.isMacOsX -> {
                createPackTask(project, TASK_PACK_DMG_NAME, hasApplicationPlugin)
                createPackTask(project, TASK_PACK_PKG_NAME, hasApplicationPlugin)
            }
            os.isLinux -> {
                createPackTask(project, TASK_PACK_DEB_NAME, hasApplicationPlugin)
                createPackTask(project, TASK_PACK_RPM_NAME, hasApplicationPlugin)
            }
        }

        project.afterEvaluate {
            packagingExtension.appVersion.convention(project.version.toString())
            packagingExtension.appName.convention(project.name)
            packagingExtension.mainJar.convention("${project.name}-${project.version}.jar")
            if (hasApplicationPlugin) {
                val application =
                    project.extensions.getByName<JavaApplication>(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
                packagingExtension.appName.convention(application.applicationName)
                packagingExtension.inputDirectory.convention(
                    project.layout.buildDirectory.dir("install/${application.applicationName}/lib")
                )
                packagingExtension.mainClass.convention(application.mainClass)
            }

            val commandLineMap = mutableMapOf<String, String?>()
            when {
                os.isWindows -> {
                    commandLineMap.append(packagingExtension.windowsOptions)
                    modifyPackTask(project, TASK_PACK_EXE_NAME, commandLineMap, packagingExtension, detectorExtension)
                    modifyPackTask(project, TASK_PACK_MSI_NAME, commandLineMap, packagingExtension, detectorExtension)
                }
                os.isMacOsX -> {
                    commandLineMap.append(packagingExtension.macOptions)
                    modifyPackTask(project, TASK_PACK_DMG_NAME, commandLineMap, packagingExtension, detectorExtension)
                    modifyPackTask(project, TASK_PACK_PKG_NAME, commandLineMap, packagingExtension, detectorExtension)
                }
                os.isLinux -> {
                    commandLineMap.append(packagingExtension.linuxOptions)
                    modifyPackTask(project, TASK_PACK_DEB_NAME, commandLineMap, packagingExtension, detectorExtension)
                    modifyPackTask(project, TASK_PACK_RPM_NAME, commandLineMap, packagingExtension, detectorExtension)
                }
            }
        }
    }

    private fun createPackTask(project: Project, taskName: String, hasApplicationPlugin: Boolean) {
        project.tasks.register<Exec>(taskName) {
            if (hasApplicationPlugin) {
                dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
            }
            group = PACKAGING_GROUP
            description = "Bundles the project as a native package in platform-specific format."
        }
    }

    private fun modifyPackTask(
        project: Project,
        taskName: String,
        commandLineMap: MutableMap<String, String?>,
        packaging: PackagingExtension,
        detector: OsDetector
    ) {
        val jpackageType = taskName.takeLast(3).toLowerCase()
        val outputFile = packaging.outputDirectory.asFile.get()
            .resolve("${packaging.appName.get()}-${packaging.appVersion.get()}.$jpackageType")
        project.tasks.getByName<Exec>(taskName) {
            val lines = mutableListOf("jpackage", "--type", jpackageType)
            commandLineMap.forEach { (key, value) ->
                lines += key
                if (value != null) {
                    lines += value
                }
            }
            commandLine(lines)
            doLast {
                outputFile.renameTo(
                    outputFile.parentFile.resolve(
                        packaging.appName.get().replace(" ", "").toLowerCase() +
                            "-${packaging.appVersion.get()}" +
                            "-${detector.arch.replace("_", "")}" +
                            ".$jpackageType"
                    )
                )
            }
        }
    }

    private fun MutableMap<String, String?>.append(packaging: Packaging) {
        put("--app-version", packaging.appVersion.get())
        put("--name", packaging.appName.get())
        put("--dest", packaging.outputDirectory.asFile.get().absolutePath)
        put("--input", packaging.inputDirectory.asFile.get().absolutePath)
        put("--main-class", packaging.mainClass.get())
        put("--main-jar", packaging.mainJar.get())
        packaging.copyright.orNull?.let { put("--copyright", it) }
        packaging.appDescription.orNull?.let { put("--description", it) }
        packaging.vendor.orNull?.let { put("--vendor", it) }
        if (packaging.verbose.get()) {
            put("--verbose", null)
        }
        packaging.addModules.orNull?.forEach { put("--add-modules", it) }
        packaging.modulePath.orNull?.forEach { put("--module-path", it.absolutePath) }
        packaging.bindServices.orNull?.let { put("--bind-services", it) }
        packaging.runtimeImage.orNull?.let { put("--runtime-image", it.asFile.absolutePath) }
        packaging.icon.orNull?.let { put("--icon", it.asFile.absolutePath) }
        packaging.addLauncher.orNull?.let { put("--add-launcher", it.asFile.absolutePath) }
        packaging.arguments.orNull?.forEach { put("--arguments", "'$it'") }
        packaging.javaOptions.orNull?.forEach { put("--java-options", it) }
        packaging.module.orNull?.let { put("--module", it) }
        packaging.appImage.orNull?.let { put("--app-image", it.asFile.absolutePath) }
        packaging.fileAssociations.orNull?.let { put("--file-associations", it.asFile.absolutePath) }
        packaging.installDirectory.orNull?.let { put("--install-dir", it.asFile.absolutePath) }
        packaging.licenseFile.orNull?.let { put("--license-file", it.asFile.absolutePath) }
        packaging.resourcesDirectory.orNull?.let { put("--resource-dir", it.asFile.absolutePath) }

        when (packaging) {
            is WindowsPackaging -> {
                if (packaging.console.isPresent && packaging.console.get()) {
                    put("--win-console", null)
                }
                if (packaging.directoryChooser.isPresent && packaging.directoryChooser.get()) {
                    put("--win-dir-chooser", null)
                }
                if (packaging.menu.isPresent && packaging.menu.get()) {
                    put("--win-menu", null)
                }
                packaging.menuGroup.orNull?.let { put("--win-menu-group", it) }
                if (packaging.perUserInstall.isPresent && packaging.perUserInstall.get()) {
                    put("--win-per-user-install", null)
                }
                if (packaging.shortcut.isPresent && packaging.shortcut.get()) {
                    put("--win-shortcut", null)
                }
                packaging.upgradeUUID.orNull?.let { put("--win-upgrade-uuid", it) }
            }
            is MacPackaging -> {
                packaging.packageIdentifier.orNull?.let { put("--mac-package-identifier", it) }
                packaging.packageName.orNull?.let { put("--mac-package-name", it) }
                packaging.bundleSigningPrefix.orNull?.let { put("--mac-bundle-signing-prefix", it) }
                if (packaging.sign.isPresent && packaging.sign.get()) {
                    put("--mac-sign", null)
                }
                packaging.signingKeychain.orNull?.let { put("--mac-signing-keychain", it.asFile.absolutePath) }
                packaging.signingKeyUserName.orNull?.let { put("--mac-signing-key-user-name", it) }
            }
            is LinuxPackaging -> {
                packaging.packageName.orNull?.let { put("--linux-package-name", it) }
                packaging.debMaintainer.orNull?.let { put("--linux-deb-maintainer", it) }
                packaging.menuGroup.orNull?.let { put("--linux-menu-group", it) }
                packaging.packageDependencies.orNull?.let { put("--linux-package-deps", it) }
                packaging.rpmLicenseType.orNull?.let { put("--linux-rpm-license-type", it) }
                packaging.appRelease.orNull?.let { put("--linux-app-release", it) }
                packaging.appCategory.orNull?.let { put("--linux-app-category", it) }
                if (packaging.shortcut.isPresent && packaging.shortcut.get()) {
                    put("--linux-shortcut", null)
                }
            }
        }
    }
}
