package com.moowork.gradle.node.task

import com.moowork.gradle.AbstractTaskTest
import com.moowork.gradle.node.NodePlugin
import org.gradle.api.Project

abstract class AbstractNodeTaskTest
    extends AbstractTaskTest
{
    @Override
    def setupProject( final Project project )
    {
        project.apply plugin: NodePlugin
    }
}
