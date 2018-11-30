package com.hendraanggrian.packr

import org.junit.Test

class PackrExtensionTest {

    @Test
    void extension() {
        def ext = new PackrExtension()

        ext.executable 'a'
        assert ext.executable == 'a'

        ext.mainClass 'b'
        assert ext.mainClass == 'b'

        ext.classpath 'a', 'b', 'c'
        assert ext.classpath.contains('a') && ext.classpath.contains('b') && ext.classpath.contains('c')

        ext.resources new File('a'), new File('b'), new File('c')
        assert ext.resources.size() == 3

        ext.minimizeJre PackrExtension.MINIMIZE_ORACLEJRE8
        assert ext.minimizeJre == PackrExtension.MINIMIZE_ORACLEJRE8
        ext.minimizeJre PackrExtension.MINIMIZE_HARD
        assert ext.minimizeJre == PackrExtension.MINIMIZE_HARD

        ext.outputDir new File('a')
        assert ext.getOutputDirectory.name == 'a'

        ext.verbose true
        assert ext.verbose

        ext.openOnDone true
        assert ext.openOnDone

        ext.macOS()
        ext.windows32()
        ext.windows64()
        ext.linux32()
        ext.linux64()
        assert ext.distributions.size() == 5
    }
}