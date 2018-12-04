package org.apache.maven.model.jdom.it;

import static org.apache.maven.model.jdom.etl.ModelETLRequest.UNIX_LS;

import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.jdom.JDomDependency;
import org.apache.maven.model.jdom.JDomDependencyManagement;
import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Test;

/**
 * Tests transformations of {@code dependencies} in {@code dependencyManagement}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class DependencyManagementEtlIT extends DependenciesEtlIT
{
    @Test
    public void addJDomDependency() throws IOException
    {
        jDomModelETL.getModel().getDependencyManagement().addDependency(
            new JDomDependency(
                new Element( "dependency" )
                    .addContent( new Text( UNIX_LS + "        " ) )
                    .addContent( new Element( "groupId" ).addContent( "org.apache.commons" ) )
                    .addContent( new Text( UNIX_LS + "        " ) )
                    .addContent( new Element( "artifactId" ).addContent( "commons-text" ) )
                    .addContent( new Text( UNIX_LS + "        " ) )
                    .addContent( new Element( "version" ).addContent( "1.6" ) )
                    .addContent( new Text( UNIX_LS + "      " ) )
            )
        );
        assertTransformation();
    }

    @Test
    public void addModelDependency() throws IOException
    {
        Dependency dependency = new Dependency();
        dependency.setGroupId( "org.apache.commons" );
        dependency.setArtifactId( "commons-exec" );
        dependency.setVersion( "1.3" );
        jDomModelETL.getModel().getDependencyManagement().addDependency( dependency );
        assertTransformation();
    }

    @Test
    public void newJDomDependencyManagement() throws IOException
    {
        jDomModelETL.getModel().setDependencyManagement(
            new JDomDependencyManagement(
                new Element( "dependencyManagement" )
                    .addContent( new Text( UNIX_LS + "    " ) )
                    .addContent(
                        new Element( "dependencies" )
                            .addContent( new Text( UNIX_LS + "      " ) )
                            .addContent(
                                new Element( "dependency" )
                                    .addContent( new Text( UNIX_LS + "        " ) )
                                    .addContent( new Element( "groupId" ).addContent( "org.apache.commons" ) )
                                    .addContent( new Text( UNIX_LS + "        " ) )
                                    .addContent( new Element( "artifactId" ).addContent( "commons-text" ) )
                                    .addContent( new Text( UNIX_LS + "        " ) )
                                    .addContent( new Element( "version" ).addContent( "1.6" ) )
                                    .addContent( new Text( UNIX_LS + "      " ) ) )
                            .addContent( new Text( UNIX_LS + "    " ) ) )
                    .addContent( new Text( UNIX_LS + "  " ) )
            )
        );
        assertTransformation();
    }

    @Test
    public void newModelDependencyManagement() throws IOException
    {
        Dependency dependency = new Dependency();
        dependency.setGroupId( "org.apache.commons" );
        dependency.setArtifactId( "commons-exec" );
        dependency.setVersion( "1.3" );
        DependencyManagement dependencyManagement = new DependencyManagement();
        dependencyManagement.addDependency( dependency );
        jDomModelETL.getModel().setDependencyManagement( dependencyManagement );
        assertTransformation();
    }

    @Test
    public void removeDependency() throws IOException
    {
        DependencyManagement dependencyManagement = jDomModelETL.getModel().getDependencyManagement();
        for ( Dependency dependency : dependencyManagement.getDependencies().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getArtifactId().equals( "commons-collections4" ) )
            {
                dependencyManagement.removeDependency( dependency );
            }
        }
        assertTransformation();
    }

    @Override
    protected List<Dependency> getDependenciesFromModel()
    {
        return jDomModelETL.getModel().getDependencyManagement().getDependencies();
    }
}
