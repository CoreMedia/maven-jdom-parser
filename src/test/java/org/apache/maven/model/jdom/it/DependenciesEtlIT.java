package org.apache.maven.model.jdom.it;

import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.junit.Test;

/**
 * Tests transformations of {@code dependencies}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class DependenciesEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void changeDependencyGroupId() throws IOException
    {
        for ( Dependency dependency : getDependenciesFromModel().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getGroupId().equals( "com.coremedia.test" ) )
            {
                dependency.setGroupId( "${project.groupId}" );
            }
        }
        assertTransformation();
    }

    @Test
    public void changeDependencyVersion() throws IOException
    {
        for ( Dependency dependency : getDependenciesFromModel().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getArtifactId().equals( "my-dependency" ) )
            {
                dependency.setVersion( "1.0" );
            }
        }
        assertTransformation();
    }

    @Test
    public void renameDependency() throws IOException
    {
        for ( Dependency dependency : getDependenciesFromModel().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getArtifactId().equals( "my-dependency" ) )
            {
                dependency.setArtifactId( "my-renamed-dependency" );
            }
        }
        assertTransformation();
    }

    protected List<Dependency> getDependenciesFromModel()
    {
        return jDomModelETL.getModel().getDependencies();
    }
}
