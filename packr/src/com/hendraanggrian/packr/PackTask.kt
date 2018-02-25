package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.toPlatform
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException

open class PackTask : DefaultTask() {

    @Input var platform: String? = null
    @Input lateinit var jdk: String
    @Input lateinit var executable: String
    @Input lateinit var classpath: List<String>
    @Input lateinit var mainClass: String
    @Input lateinit var vmArgs: List<String>
    @Input lateinit var resources: List<File>
    @Input lateinit var minimizeJre: String
    @OutputDirectory lateinit var outputDir: File
    @Input var macWrapApp: Boolean = true
    @Input lateinit var macIcon: Any
    @Input lateinit var macBundle: Any

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        val config = PackrConfig(platform!!.toPlatform(), jdk, executable, classpath, mainClass, outputDir)
        config.vmArgs = vmArgs
        config.resources = resources
        config.minimizeJre = minimizeJre
        (macIcon as? File).let { config.iconResource = it }
        (macBundle as? String).let { config.bundleIdentifier = it }
        Packr().pack(config)
    }
}