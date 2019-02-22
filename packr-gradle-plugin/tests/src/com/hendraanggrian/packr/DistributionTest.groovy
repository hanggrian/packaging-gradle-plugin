package com.hendraanggrian.packr


import org.junit.Test

class DistributionTest {

    @Test
    void dist() {
        def dist = new Distribution()
        dist.name 'an app'
        dist.jdk 'a jdk'
        assert dist.name == 'an app'
        assert dist.jdk == 'a jdk'
    }

    @Test
    void mac() {
        def mac = new MacOSDistribution()
        mac.icon new File('awesome icon')
        mac.bundleId 'awesome id'
        assert mac.icon.name == 'awesome icon'
        assert mac.bundleId == 'awesome id'
    }
}