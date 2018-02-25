package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import com.badlogicgames.packr.PackrConfig.Platform
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.IOException

open class PackTask : DefaultTask() {

    @Input lateinit var platform: Platform
    @Input lateinit var configs: List<PackrConfig>

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        val config = configs.singleOrNull { it.platform == platform } ?: error("Configuration for $platform not found")
        println("Packing $platform:")
        Packr().pack(config)
    }
}