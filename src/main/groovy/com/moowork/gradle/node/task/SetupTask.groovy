package com.moowork.gradle.node.task

import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.variant.Variant
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class SetupTask
    extends DefaultTask
{
    public final static String NAME = 'nodeSetup'

    private NodeExtension config

    protected Variant variant

    private IvyArtifactRepository repo

    private List<ArtifactRepository> allRepos;

    SetupTask()
    {
        this.group = 'Node'
        this.description = 'Download and install a local node/npm version.'
        this.enabled = false
    }

    @Input
    public Set<String> getInput()
    {
        configureIfNeeded()

        def set = new HashSet<>()
        set.add( this.config.download )
        set.add( this.variant.tarGzDependency )
        set.add( this.variant.exeDependency )
        return set
    }

    @OutputDirectory
    public File getNodeDir()
    {
        configureIfNeeded()
        return this.variant.nodeDir
    }

    private void configureIfNeeded()
    {
        if ( this.config != null )
        {
            return
        }

        this.config = NodeExtension.get( this.project )
        this.variant = this.config.variant
    }

    @TaskAction
    void exec()
    {
        configureIfNeeded()
        addRepository()

        if ( this.variant.windows )
        {
            copyNodeExe()
        }

        unpackNodeTarGz()
        setExecutableFlag()
        restoreRepositories()
    }

    private void copyNodeExe()
    {
        this.project.copy {
            from getNodeExeFile()
            into this.variant.nodeBinDir
            rename 'node.+\\.exe', 'node.exe'
        }
    }

    private void unpackNodeTarGz()
    {
        this.project.copy {
            from this.project.tarTree( getNodeTarGzFile() )
            into getNodeDir().parent
        }
    }

    private void setExecutableFlag()
    {
        if ( !this.variant.windows )
        {
            new File( this.variant.nodeExec ).setExecutable( true )
        }
    }

    protected File getNodeExeFile()
    {
        return resloveSingle( this.variant.exeDependency )
    }

    protected File getNodeTarGzFile()
    {
        return resloveSingle( this.variant.tarGzDependency )
    }

    private File resloveSingle( String name )
    {
        def dep = this.project.dependencies.create( name )
        def conf = this.project.configurations.detachedConfiguration( dep )
        conf.transitive = false
        return conf.resolve().iterator().next();
    }

    private void addRepository()
    {
        this.allRepos = new ArrayList<>()
        this.allRepos.addAll( this.project.repositories )
        this.project.repositories.clear()

        def distUrl = this.config.distBaseUrl
        this.repo = this.project.repositories.ivy {
            url distUrl
            layout 'pattern', {
                artifact 'v[revision]/[artifact](-v[revision]-[classifier]).[ext]'
            }
        }

        if ( this.config.distCredentialsAction != null )
        {
            this.repo.credentials( this.config.distCredentialsType, this.config.distCredentialsAction )
        }
    }

    private void restoreRepositories()
    {
        this.project.repositories.clear();
        this.project.repositories.addAll( this.allRepos );
    }
}
