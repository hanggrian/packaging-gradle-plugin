package com.hendraanggrian.packr.dist

import org.gradle.api.Project
import java.io.File

internal class MacOSDistribution(project: Project) : Distribution(project), MacOSDistributionBuilder {

    override var icon: File? = null

    override var bundleId: String? = null
}