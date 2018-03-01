package com.hendraanggrian.packr

import org.gradle.api.Project
import java.io.File

/**
 * Packr main extension, modifications of this POJO will be applied to all [PackTask].
 * It is not mandatory to modify this extension, configuring each [PackTask] will result in same behavior.
 */
open class PackrExtension(private val project: Project) : PackrConfiguration {

    override var jdk: String? = ""
    override var executable: String? = project.name
    override var classpath: MutableList<String> = mutableListOf()
    override var mainClass: String? = ""
    override var vmArgs: MutableList<String> = mutableListOf()
    override var resources: MutableList<File> = mutableListOf()
    override var minimizeJre: String? = "soft"
    override var outputName: String? = project.name
    override var outputDir: File? = project.buildDir.resolve("release")

    override var iconDir: File? = null
    override var bundleId: String? = null
    override var verbose: Boolean? = false
    override var openOnDone: Boolean? = false

    override fun getProject(): Project = project

    internal infix fun applyTo(task: PackrConfiguration) {
        if (task.jdk == null) task.jdk = jdk
        if (task.executable == null) task.executable = executable
        if (task.classpath.isEmpty()) task.classpath(*classpath.toTypedArray())
        if (task.mainClass == null) task.mainClass = mainClass
        if (task.vmArgs.isEmpty()) task.vmArgs(*vmArgs.toTypedArray())
        if (task.resources.isEmpty()) task.resources(*resources.toTypedArray())
        if (task.minimizeJre == null) task.minimizeJre = minimizeJre
        if (task.outputName == null) task.outputName = outputName
        if (task.outputDir == null) task.outputDir = outputDir

        if (task.iconDir == null) task.iconDir = iconDir
        if (task.bundleId == null) task.bundleId = bundleId
        if (task.verbose == null) task.verbose = verbose
        if (task.openOnDone == null) task.openOnDone = openOnDone
    }
}