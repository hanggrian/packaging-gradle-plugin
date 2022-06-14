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

/**
 * Plugin that creates native bundles for your JAR.
 *
 * @see <a href="https://github.com/hendraanggrian/packaging-gradle-plugin">packaging-gradle-plugin</a>
 */
class PackagingPlugin : Plugin<Project> {
    companion object {
        const val GROUP = "packaging"
        const val TASK_PACK_EXE = "packExe"
        const val TASK_PACK_MSI = "packMsi"
        const val TASK_PACK_DMG = "packDmg"
        const val TASK_PACK_PKG = "packPkg"
        const val TASK_PACK_DEB = "packDeb"
        const val TASK_PACK_RPM = "packRpm"
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(OsDetectorPlugin::class)
        val hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)

        val packaging = project.extensions.create(PackagingExtension::class, "packaging", DefaultPackagingExtension::class, project)
        val osDetector = project.extensions.getByType<OsDetector>()
        val os = DefaultNativePlatform.getCurrentOperatingSystem()
        when {
            os.isWindows -> {
                createPackTask(project, TASK_PACK_EXE, hasApplicationPlugin)
                createPackTask(project, TASK_PACK_MSI, hasApplicationPlugin)
            }
            os.isMacOsX -> {
                createPackTask(project, TASK_PACK_DMG, hasApplicationPlugin)
                createPackTask(project, TASK_PACK_PKG, hasApplicationPlugin)
            }
            os.isLinux -> {
                createPackTask(project, TASK_PACK_DEB, hasApplicationPlugin)
                createPackTask(project, TASK_PACK_RPM, hasApplicationPlugin)
            }
        }

        project.afterEvaluate {
            packaging.appVersion.convention(project.version.toString())
            packaging.appName.convention(project.name)
            packaging.mainJar.convention("${project.name}-${project.version}.jar")
            if (hasApplicationPlugin) {
                val application = project.extensions
                    .getByName<JavaApplication>(ApplicationPlugin.APPLICATION_PLUGIN_NAME)
                packaging.appName.convention(application.applicationName)
                packaging.inputDirectory.convention(
                    project.layout.buildDirectory.dir("install/${application.applicationName}/lib")
                )
                packaging.mainClass.convention(application.mainClass)
            }

            val commandLines = mutableListOf<String>()
            when {
                os.isWindows -> {
                    commandLines.append(packaging.windowsSpec.get())
                    modifyPackTask(project, TASK_PACK_EXE, commandLines, packaging, osDetector)
                    modifyPackTask(project, TASK_PACK_MSI, commandLines, packaging, osDetector)
                }
                os.isMacOsX -> {
                    commandLines.append(packaging.macSpec.get())
                    modifyPackTask(project, TASK_PACK_DMG, commandLines, packaging, osDetector)
                    modifyPackTask(project, TASK_PACK_PKG, commandLines, packaging, osDetector)
                }
                os.isLinux -> {
                    commandLines.append(packaging.linuxSpec.get())
                    modifyPackTask(project, TASK_PACK_DEB, commandLines, packaging, osDetector)
                    modifyPackTask(project, TASK_PACK_RPM, commandLines, packaging, osDetector)
                }
            }
        }
    }

    private fun createPackTask(project: Project, taskName: String, hasApplicationPlugin: Boolean) {
        project.tasks.register<Exec>(taskName) {
            if (hasApplicationPlugin) {
                dependsOn(DistributionPlugin.TASK_INSTALL_NAME)
            }
            group = GROUP
            description = "Bundles the project as a native package in platform-specific format."
        }
    }

    private fun modifyPackTask(
        project: Project,
        taskName: String,
        commandLines: List<String>,
        packaging: PackagingExtension,
        detector: OsDetector
    ) {
        val jpackageType = taskName.takeLast(3).toLowerCase()
        val outputFile = packaging.outputDirectory.asFile.get()
            .resolve("${packaging.appName.get()}-${packaging.appVersion.get()}.$jpackageType")
        project.tasks.getByName<Exec>(taskName) {
            commandLine(listOf("jpackage", "--type", jpackageType) + commandLines)
            doLast {
                outputFile.renameTo(
                    outputFile.parentFile.resolve(
                        packaging.appName.get().replace(" ", "") +
                            "-${packaging.appVersion.get()}" +
                            "-${denormalizeArch(detector.arch)}" +
                            ".$jpackageType"
                    )
                )
            }
        }
    }

    private fun MutableList<String>.append(packSpec: PackSpec) {
        add("--app-version"); add(packSpec.appVersion.get())
        add("--name"); add(packSpec.appName.get())
        add("--dest"); add(packSpec.outputDirectory.asFile.get().absolutePath)
        add("--input"); add(packSpec.inputDirectory.asFile.get().absolutePath)
        add("--main-class"); add(packSpec.mainClass.get())
        add("--main-jar"); add(packSpec.mainJar.get())
        packSpec.copyright.orNull?.let { add("--copyright"); add(it) }
        packSpec.appDescription.orNull?.let { add("--description"); add(it) }
        packSpec.vendor.orNull?.let { add("--vendor"); add(it) }
        if (packSpec.verbose.get()) {
            add("--verbose")
        }
        packSpec.modules.orNull?.forEach { add("--add-modules"); add(it) }
        packSpec.modulePaths.orNull?.forEach { add("--module-path"); add(it.absolutePath) }
        packSpec.bindServices.orNull?.let { add("--bind-services"); add(it) }
        packSpec.runtimeImage.orNull?.let { add("--runtime-image"); add(it.asFile.absolutePath) }
        packSpec.icon.orNull?.let { add("--icon"); add(it.asFile.absolutePath) }
        packSpec.launcher.orNull?.let { add("--add-launcher"); add(it.asFile.absolutePath) }
        packSpec.args.orNull?.forEach { add("--arguments"); add("'$it'") }
        packSpec.javaArgs.orNull?.forEach { add("--java-options"); add(it) }
        packSpec.mainModule.orNull?.let { add("--module"); add(it) }
        packSpec.appImage.orNull?.let { add("--app-image"); add(it.asFile.absolutePath) }
        packSpec.fileAssociations.orNull?.let { add("--file-associations"); add(it.asFile.absolutePath) }
        packSpec.installDirectory.orNull?.let { add("--install-dir"); add(it.asFile.absolutePath) }
        packSpec.license.orNull?.let { add("--license-file"); add(it.asFile.absolutePath) }
        packSpec.resourcesDirectory.orNull?.let { add("--resource-dir"); add(it.asFile.absolutePath) }

        when (packSpec) {
            is WindowsPackSpec -> {
                if (packSpec.console.isPresent && packSpec.console.get()) {
                    add("--win-console")
                }
                if (packSpec.directoryChooser.isPresent && packSpec.directoryChooser.get()) {
                    add("--win-dir-chooser")
                }
                if (packSpec.menu.isPresent && packSpec.menu.get()) {
                    add("--win-menu")
                }
                packSpec.menuGroup.orNull?.let { add("--win-menu-group"); add(it) }
                if (packSpec.perUserInstall.isPresent && packSpec.perUserInstall.get()) {
                    add("--win-per-user-install")
                }
                if (packSpec.shortcut.isPresent && packSpec.shortcut.get()) {
                    add("--win-shortcut")
                }
                packSpec.upgradeUUID.orNull?.let { add("--win-upgrade-uuid"); add(it) }
            }
            is MacPackSpec -> {
                packSpec.packageIdentifier.orNull?.let { add("--mac-package-identifier"); add(it) }
                packSpec.packageName.orNull?.let { add("--mac-package-name"); add(it) }
                packSpec.bundleSigningPrefix.orNull?.let { add("--mac-bundle-signing-prefix"); add(it) }
                if (packSpec.sign.isPresent && packSpec.sign.get()) {
                    add("--mac-sign")
                }
                packSpec.signingKeychain.orNull?.let { add("--mac-signing-keychain"); add(it.asFile.absolutePath) }
                packSpec.signingKeyUserName.orNull?.let { add("--mac-signing-key-user-name"); add(it) }
            }
            is LinuxPackSpec -> {
                packSpec.packageName.orNull?.let { add("--linux-package-name"); add(it) }
                packSpec.debMaintainer.orNull?.let { add("--linux-deb-maintainer"); add(it) }
                packSpec.menuGroup.orNull?.let { add("--linux-menu-group"); add(it) }
                packSpec.packageDependencies.orNull?.let { add("--linux-package-deps"); add(it) }
                packSpec.rpmLicenseType.orNull?.let { add("--linux-rpm-license-type"); add(it) }
                packSpec.appRelease.orNull?.let { add("--linux-app-release"); add(it) }
                packSpec.appCategory.orNull?.let { add("--linux-app-category"); add(it) }
                if (packSpec.shortcut.isPresent && packSpec.shortcut.get()) {
                    add("--linux-shortcut")
                }
            }
        }
    }
}
