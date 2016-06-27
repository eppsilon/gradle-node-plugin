package com.moowork.gradle.grunt

import com.moowork.gradle.AbstractProjectTest

import java.util.concurrent.atomic.AtomicBoolean

class GruntPluginProjectSpec
    extends AbstractProjectTest
{
    def 'creates extension'()
    {
        when:
        project.apply plugin: 'com.moowork.grunt'

        then:
        project.extensions.getByName( 'grunt' )
    }

    def 'can evaluate'()
    {
        setup:
        def signal = new AtomicBoolean( false )

        project.afterEvaluate {
            signal.getAndSet( true )
        }

        project.apply plugin: 'com.moowork.grunt'

        when:
        project.evaluate()

        then:
        noExceptionThrown()
        signal.get() == true
    }
}
