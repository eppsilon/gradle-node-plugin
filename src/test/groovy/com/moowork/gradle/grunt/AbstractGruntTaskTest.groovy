package com.moowork.gradle.grunt

import com.moowork.gradle.AbstractTaskTest
import org.gradle.api.Project

abstract class AbstractGruntTaskTest
    extends AbstractTaskTest
{
    @Override
    def setupProject( final Project project )
    {
        project.apply plugin: GruntPlugin
    }
}
