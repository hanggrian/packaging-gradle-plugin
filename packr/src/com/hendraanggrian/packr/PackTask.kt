package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import com.badlogicgames.packr.PackrConfig.Platform.Linux32
import com.badlogicgames.packr.PackrConfig.Platform.Linux64
import com.badlogicgames.packr.PackrConfig.Platform.MacOS
import com.badlogicgames.packr.PackrConfig.Platform.Windows32
import com.badlogicgames.packr.PackrConfig.Platform.Windows64
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException

open class PackTask : DefaultTask() {

    @Input lateinit var macJdk: String
    @Input lateinit var windows32Jdk: String
    @Input lateinit var windows64Jdk: String
    @Input lateinit var linux32Jdk: String
    @Input lateinit var linux64Jdk: String

    @Input lateinit var executable: String
    @Input lateinit var classpath: List<String>
    @Input lateinit var mainClass: String
    @Input lateinit var vmArgs: List<String>
    @Input lateinit var resources: List<File>
    @Input lateinit var minimizeJre: String
    @OutputDirectory lateinit var outputDir: File

    @Input var macWrapApp: Boolean = true
    @Input lateinit var macIcon: String
    @Input lateinit var macBundle: String

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        val map = mutableMapOf<Platform, String>()
        if (macJdk.isNotEmpty()) map[MacOS] = macJdk
        if (windows32Jdk.isNotEmpty()) map[Windows32] = windows32Jdk
        if (windows64Jdk.isNotEmpty()) map[Windows64] = windows64Jdk
        if (linux32Jdk.isNotEmpty()) map[Linux32] = linux32Jdk
        if (linux64Jdk.isNotEmpty()) map[Linux64] = linux64Jdk
        map.forEach { platform, jdk ->
            println("Packing $platform:")
            Packr().pack(newConfig(platform, jdk))
        }
    }

    private fun newConfig(platform: Platform, jdk: String): PackrConfig {
        val config = PackrConfig()
        config.platform = platform
        config.jdk = jdk
        config.executable = executable
        config.classpath = classpath
        config.mainClass = mainClass
        config.outDir = if (platform == MacOS && macWrapApp) File(outputDir.parent, "${outputDir.name}.app") else outputDir
        config.vmArgs = vmArgs
        config.resources = resources
        config.minimizeJre = minimizeJre
        if (macIcon.isNotEmpty()) config.iconResource = File(macIcon)
        if (macBundle.isNotEmpty()) config.bundleIdentifier = macBundle
        return config
    }
}