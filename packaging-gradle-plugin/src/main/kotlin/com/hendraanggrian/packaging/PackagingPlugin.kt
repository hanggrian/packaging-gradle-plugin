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
        const val GROUP = "distribution" // the same as DistributionPlugin.DISTRIBUTION_GROUP
        const val TASK_DIST_WINDOWS_EXE = "distWindowsExe"
        const val TASK_DIST_WINDOWS_MSI = "distWindowsMsi"
        const val TASK_DIST_MAC_DMG = "distMacDmg"
        const val TASK_DIST_MAC_PKG = "distMacPkg"
        const val TASK_DIST_LINUX_DEB = "distLinuxDeb"
        const val TASK_DIST_LINUX_RPM = "distLinuxRpm"
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(OsDetectorPlugin::class)
        val hasApplicationPlugin = project.pluginManager.hasPlugin(ApplicationPlugin.APPLICATION_PLUGIN_NAME)

        val packaging = project.extensions.create(
            PackagingExtension::class, "packaging",
            DefaultPackagingExtension::class, project.objects, project.layout
        )
        val osDetector = project.extensions.getByType<OsDetector>()
        val os = DefaultNativePlatform.getCurrentOperatingSystem()
        when {
            os.isWindows -> {
                createPackTask(project, TASK_DIST_WINDOWS_EXE, hasApplicationPlugin)
                createPackTask(project, TASK_DIST_WINDOWS_MSI, hasApplicationPlugin)
            }
            os.isMacOsX -> {
                createPackTask(project, TASK_DIST_MAC_DMG, hasApplicationPlugin)
                createPackTask(project, TASK_DIST_MAC_PKG, hasApplicationPlugin)
            }
            os.isLinux -> {
                createPackTask(project, TASK_DIST_LINUX_DEB, hasApplicationPlugin)
                createPackTask(project, TASK_DIST_LINUX_RPM, hasApplicationPlugin)
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
            packaging as DefaultPackagingExtension
            when {
                os.isWindows -> {
                    commandLines.append(packaging.windowsOptions ?: packaging)
                    modifyPackTask(project, TASK_DIST_WINDOWS_EXE, commandLines, packaging, osDetector)
                    modifyPackTask(project, TASK_DIST_WINDOWS_MSI, commandLines, packaging, osDetector)
                }
                os.isMacOsX -> {
                    commandLines.append(packaging.macOptions ?: packaging)
                    modifyPackTask(project, TASK_DIST_MAC_DMG, commandLines, packaging, osDetector)
                    modifyPackTask(project, TASK_DIST_MAC_PKG, commandLines, packaging, osDetector)
                }
                os.isLinux -> {
                    commandLines.append(packaging.linuxOptions ?: packaging)
                    modifyPackTask(project, TASK_DIST_LINUX_DEB, commandLines, packaging, osDetector)
                    modifyPackTask(project, TASK_DIST_LINUX_RPM, commandLines, packaging, osDetector)
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

    private fun MutableList<String>.append(spec: PackSpec) {
        add("--app-version"); add(spec.appVersion.get())
        add("--name"); add(spec.appName.get())
        add("--dest"); add(spec.outputDirectory.asFile.get().absolutePath)
        add("--input"); add(spec.inputDirectory.asFile.get().absolutePath)
        add("--main-class"); add(spec.mainClass.get())
        add("--main-jar"); add(spec.mainJar.get())
        spec.copyright.orNull?.let { add("--copyright"); add(it) }
        spec.appDescription.orNull?.let { add("--description"); add(it) }
        spec.vendor.orNull?.let { add("--vendor"); add(it) }
        if (spec.verbose.get()) {
            add("--verbose")
        }
        spec.modules.orNull?.forEach { add("--add-modules"); add(it) }
        spec.modulePaths.orNull?.forEach { add("--module-path"); add(it.absolutePath) }
        spec.bindServices.orNull?.let { add("--bind-services"); add(it) }
        spec.runtimeImage.orNull?.let { add("--runtime-image"); add(it.asFile.absolutePath) }
        spec.icon.orNull?.let { add("--icon"); add(it.asFile.absolutePath) }
        spec.launcher.orNull?.let { add("--add-launcher"); add(it.asFile.absolutePath) }
        spec.args.orNull?.forEach { add("--arguments"); add("'$it'") }
        spec.javaArgs.orNull?.forEach { add("--java-options"); add(it) }
        spec.mainModule.orNull?.let { add("--module"); add(it) }
        spec.appImage.orNull?.let { add("--app-image"); add(it.asFile.absolutePath) }
        spec.fileAssociations.orNull?.let { add("--file-associations"); add(it.asFile.absolutePath) }
        spec.installDirectory.orNull?.let { add("--install-dir"); add(it.asFile.absolutePath) }
        spec.license.orNull?.let { add("--license-file"); add(it.asFile.absolutePath) }
        spec.resourcesDirectory.orNull?.let { add("--resource-dir"); add(it.asFile.absolutePath) }

        when (spec) {
            is WindowsOptions -> {
                if (spec.console) add("--win-console")
                if (spec.directoryChooser) add("--win-dir-chooser")
                if (spec.menu) add("--win-menu")
                spec.menuGroup?.let { add("--win-menu-group"); add(it) }
                if (spec.perUserInstall) add("--win-per-user-install")
                if (spec.shortcut) add("--win-shortcut")
                spec.upgradeUUID?.let { add("--win-upgrade-uuid"); add(it) }
            }
            is MacOptions -> {
                spec.packageIdentifier?.let { add("--mac-package-identifier"); add(it) }
                spec.packageName?.let { add("--mac-package-name"); add(it) }
                spec.bundleSigningPrefix?.let { add("--mac-bundle-signing-prefix"); add(it) }
                if (spec.sign) add("--mac-sign")
                spec.signingKeychain?.let { add("--mac-signing-keychain"); add(it.absolutePath) }
                spec.signingKeyUserName?.let { add("--mac-signing-key-user-name"); add(it) }
            }
            is LinuxOptions -> {
                spec.packageName?.let { add("--linux-package-name"); add(it) }
                spec.debMaintainer?.let { add("--linux-deb-maintainer"); add(it) }
                spec.menuGroup?.let { add("--linux-menu-group"); add(it) }
                spec.packageDependencies?.let { add("--linux-package-deps"); add(it) }
                spec.rpmLicenseType?.let { add("--linux-rpm-license-type"); add(it) }
                spec.appRelease?.let { add("--linux-app-release"); add(it) }
                spec.appCategory?.let { add("--linux-app-category"); add(it) }
                if (spec.shortcut) add("--linux-shortcut")
            }
        }
    }
}
