package com.hendraanggrian.packaging

import com.hendraanggrian.packaging.internal.DefaultPackagingExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/** Plugin that creates native bundles for your JAR. */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val PLUGIN_NAME = "packaging"
        const val GROUP_NAME = PLUGIN_NAME
    }

    override fun apply(project: Project) {
        val hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)

        val extension = project.extensions.create(
            PackagingExtension::class, "packaging", DefaultPackagingExtension::class, project
        )

        val packExe by project.tasks.registering(Exec::class) { setup(hasApplicationPlugin) }
        val packMsi by project.tasks.registering(Exec::class) { setup(hasApplicationPlugin) }
        val packDmg by project.tasks.registering(Exec::class) { setup(hasApplicationPlugin) }
        val packPkg by project.tasks.registering(Exec::class) { setup(hasApplicationPlugin) }
        val packDeb by project.tasks.registering(Exec::class) { setup(hasApplicationPlugin) }
        val packRpm by project.tasks.registering(Exec::class) { setup(hasApplicationPlugin) }

        project.afterEvaluate {
            extension.appVersion.convention(project.version.toString())
            extension.appName.convention(project.name)
            extension.mainJar.convention("${project.name}-${project.version}.jar")
            if (hasApplicationPlugin) {
                val application = project.extensions
                    .getByName<JavaApplication>(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
                extension.appName.convention(application.applicationName)
                extension.inputDirectory.convention(
                    project.layout.buildDirectory.dir("install/${application.applicationName}/lib")
                )
                extension.mainClass.convention(application.mainClass)
            }

            val startingLines = getStartingLines(extension)
            val windowsLines = getWindowsLines(extension)
            val macLines = getMacLines(extension)
            val linuxLines = getLinuxLines(extension)
            packExe { commandLine(startingLines + getTypeLines("exe") + windowsLines) }
            packMsi { commandLine(startingLines + getTypeLines("msi") + windowsLines) }
            packDmg { commandLine(startingLines + getTypeLines("dmg") + macLines) }
            packPkg { commandLine(startingLines + getTypeLines("pkg") + macLines) }
            packDeb { commandLine(startingLines + getTypeLines("deb") + linuxLines) }
            packRpm { commandLine(startingLines + getTypeLines("rpm") + linuxLines) }
        }
    }

    private fun Exec.setup(hasApplicationPlugin: Boolean) {
        if (hasApplicationPlugin) {
            dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
        }
        group = GROUP_NAME
        description = "Bundles the project as a native package in platform-specific format."
    }

    private fun getStartingLines(extension: PackagingExtension): List<String> {
        val lines = mutableListOf(
            "jpackage",
            "--app-version", extension.appVersion.get(),
            "--name", extension.appName.get(),
            "--dest", extension.outputDirectory.asFile.get().absolutePath,
            "--input", extension.inputDirectory.asFile.get().absolutePath,
            "--main-class", extension.mainClass.get(),
            "--main-jar", extension.mainJar.get(),
        )
        extension.copyright.orNull?.let {
            lines += "--copyright"
            lines += it
        }
        extension.appDescription.orNull?.let {
            lines += "--description"
            lines += it
        }
        extension.vendor.orNull?.let {
            lines += "--vendor"
            lines += it
        }
        if (extension.verbose.get()) {
            lines += "--verbose"
        }
        extension.addModules.orNull?.forEach {
            lines += "--add-modules"
            lines += it
        }
        extension.modulePath.orNull?.forEach {
            lines += "--module-path"
            lines += it.absolutePath
        }
        extension.bindServices.orNull?.let {
            lines += "--bind-services"
            lines += it
        }
        extension.runtimeImage.orNull?.let {
            lines += "--runtime-image"
            lines += it.asFile.absolutePath
        }
        extension.icon.orNull?.let {
            lines += "--icon"
            lines += it.asFile.absolutePath
        }
        extension.addLauncher.orNull?.let {
            lines += "--add-launcher"
            lines += it.asFile.absolutePath
        }
        extension.arguments.orNull?.forEach {
            lines += "--arguments"
            lines += "'$it'"
        }
        extension.javaOptions.orNull?.forEach {
            lines += "--java-options"
            lines += it
        }
        extension.module.orNull?.let {
            lines += "--module"
            lines += it
        }
        extension.appImage.orNull?.let {
            lines += "--app-image"
            lines += it.asFile.absolutePath
        }
        extension.fileAssociations.orNull?.let {
            lines += "--file-associations"
            lines += it.asFile.absolutePath
        }
        extension.installDirectory.orNull?.let {
            lines += "--install-dir"
            lines += it.asFile.absolutePath
        }
        extension.licenseFile.orNull?.let {
            lines += "--license-file"
            lines += it.asFile.absolutePath
        }
        extension.resourcesDirectory.orNull?.let {
            lines += "--resource-dir"
            lines += it.asFile.absolutePath
        }
        return lines
    }

    private fun getTypeLines(type: String): List<String> = listOf("--type", type)

    private fun getWindowsLines(extension: PackagingExtension): List<String> {
        val lines = mutableListOf<String>()
        if (extension.windowsOptions.console.isPresent && extension.windowsOptions.console.get()) {
            lines += "--win-console"
        }
        if (extension.windowsOptions.directoryChooser.isPresent && extension.windowsOptions.directoryChooser.get()) {
            lines += "--win-dir-chooser"
        }
        if (extension.windowsOptions.menu.isPresent && extension.windowsOptions.menu.get()) {
            lines += "--win-menu"
        }
        extension.windowsOptions.menuGroup.orNull?.let {
            lines += "--win-menu-group"
            lines += it
        }
        if (extension.windowsOptions.perUserInstall.isPresent && extension.windowsOptions.perUserInstall.get()) {
            lines += "--win-per-user-install"
        }
        if (extension.windowsOptions.shortcut.isPresent && extension.windowsOptions.shortcut.get()) {
            lines += "--win-shortcut"
        }
        extension.windowsOptions.upgradeUUID.orNull?.let {
            lines += "--win-upgrade-uuid"
            lines += it
        }
        return lines
    }

    private fun getMacLines(extension: PackagingExtension): List<String> {
        val lines = mutableListOf<String>()
        extension.macOptions.packageIdentifier.orNull?.let {
            lines += "--mac-package-identifier"
            lines += it
        }
        extension.macOptions.packageName.orNull?.let {
            lines += "--mac-package-name"
            lines += it
        }
        extension.macOptions.bundleSigningPrefix.orNull?.let {
            lines += "--mac-bundle-signing-prefix"
            lines += it
        }
        if (extension.macOptions.sign.isPresent && extension.macOptions.sign.get()) {
            lines += "--mac-sign"
        }
        extension.macOptions.signingKeychain.orNull?.let {
            lines += "--mac-signing-keychain"
            lines += it.asFile.absolutePath
        }
        extension.macOptions.signingKeyUserName.orNull?.let {
            lines += "--mac-signing-key-user-name"
            lines += it
        }
        return lines
    }

    private fun getLinuxLines(packaging: PackagingExtension): List<String> {
        val lines = mutableListOf<String>()
        packaging.linuxOptions.packageName.orNull?.let {
            lines += "--linux-package-name"
            lines += it
        }
        packaging.linuxOptions.debMaintainer.orNull?.let {
            lines += "--linux-deb-maintainer"
            lines += it
        }
        packaging.linuxOptions.menuGroup.orNull?.let {
            lines += "--linux-menu-group"
            lines += it
        }
        packaging.linuxOptions.packageDependencies.orNull?.let {
            lines += "--linux-package-deps"
            lines += it
        }
        packaging.linuxOptions.rpmLicenseType.orNull?.let {
            lines += "--linux-rpm-license-type"
            lines += it
        }
        packaging.linuxOptions.appRelease.orNull?.let {
            lines += "--linux-app-release"
            lines += it
        }
        packaging.linuxOptions.appCategory.orNull?.let {
            lines += "--linux-app-category"
            lines += it
        }
        if (packaging.linuxOptions.shortcut.isPresent && packaging.linuxOptions.shortcut.get()) {
            lines += "--linux-shortcut"
        }
        return lines
    }
}
