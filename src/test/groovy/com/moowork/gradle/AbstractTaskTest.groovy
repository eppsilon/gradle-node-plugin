package com.moowork.gradle

import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.util.PlatformHelper
import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

abstract class AbstractTaskTest
    extends AbstractProjectTest
{
    def ExecResult execResult

    def ExecSpec execSpec

    def Properties props

    def NodeExtension ext

    def setup()
    {
        this.props = new Properties()
        PlatformHelper.INSTANCE = new PlatformHelper( this.props )

        this.execResult = Mock( ExecResult )

        setupProject( this.project )
        this.ext = NodeExtension.get( this.project )

        mockExec()
    }

    def abstract setupProject( Project project );

    private void mockExec()
    {
        this.project.metaClass.invokeMethod = { String name, Object[] args ->
            if ( name == 'exec' )
            {
                Closure closure = (Closure) args.first()
                closure.call( this.execSpec )
                return this.execResult
            }
            else
            {
                MetaMethod metaMethod = delegate.class.metaClass.getMetaMethod( name, args )
                return metaMethod?.invoke( delegate, args )
            }
        }
    }
}
