package com.moowork.gradle.gulp

import com.moowork.gradle.AbstractProjectTest

import java.util.concurrent.atomic.AtomicBoolean

class GulpPluginProjectSpec
    extends AbstractProjectTest
{
    def 'creates extension'()
    {
        when:
        project.apply plugin: 'com.moowork.gulp'

        then:
        project.extensions.getByName( 'gulp' )
    }

    def 'can evaluate'()
    {
        setup:
        def signal = new AtomicBoolean( false )

        project.afterEvaluate {
            signal.getAndSet( true )
        }

        project.apply plugin: 'com.moowork.gulp'

        when:
        project.evaluate()

        then:
        noExceptionThrown()
        signal.get() == true
    }
}
