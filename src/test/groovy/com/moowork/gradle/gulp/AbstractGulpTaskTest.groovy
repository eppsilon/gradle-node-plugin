package com.moowork.gradle.gulp

import com.moowork.gradle.AbstractTaskTest
import org.gradle.api.Project

abstract class AbstractGulpTaskTest
    extends AbstractTaskTest
{
    @Override
    def setupProject( final Project project )
    {
        project.apply plugin: GulpPlugin
    }
}
