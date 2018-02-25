package com.hendraanggrian.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.IOException

open class PackTask : DefaultTask() {

    @Input lateinit var config: PackrConfig

    @TaskAction
    @Throws(IOException::class)
    fun pack() {
        config.outDir.deleteRecursively()
        Packr().pack(config)
    }
}