package com.hendraanggrian.packr.dist

internal class MacOSDistribution : Distribution(), MacOSDistributionBuilder {

    override var icon: String? = null

    override var bundleId: String? = null
}