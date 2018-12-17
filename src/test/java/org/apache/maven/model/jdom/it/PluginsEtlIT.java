package org.apache.maven.model.jdom.it;

import static org.apache.maven.model.jdom.etl.ModelETLRequest.UNIX_LS;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.jdom.JDomConfiguration;
import org.apache.maven.model.jdom.JDomDependency;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Test;

/**
 * Tests transformations of {@code plugins}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PluginsEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void changeConfigurationValue() throws IOException
    {
        // TODO We should add a class JDomConfiguration
        ( (Xpp3Dom) getPluginsFromModel().get( 0 ).getConfiguration() ).getChild( "skipDeploy" ).setValue( "true" );
        assertTransformation();
    }

    @Test
    public void changePluginVersion() throws IOException
    {
        for ( Plugin plugin : getPluginsFromModel().toArray( new Plugin[]{} ) )
        {
            if ( plugin.getArtifactId().equals( "maven-site-plugin" ) )
            {
                plugin.setVersion( "3.7.1" );
            }
        }
        assertTransformation();
    }

    @Test
    public void setJDomConfiguration() throws IOException
    {
        // TODO We should add a class JDomConfiguration
        getPluginsFromModel().get( 0 ).setConfiguration(
            new JDomConfiguration(
                new Element( "configuration", "http://maven.apache.org/POM/4.0.0" )
                    .addContent( new Text( UNIX_LS + "            " ) )
                    .addContent( new Element( "skipDeploy", "http://maven.apache.org/POM/4.0.0" )
                        .addContent( "true" ) )
                    .addContent( new Text( UNIX_LS + "          " ) )
            )
        );
        assertTransformation();
    }

    @Test
    public void setJDomDependencies() throws IOException
    {
        // TODO We should add a class JDomDependencies
        getPluginsFromModel().get( 0 ).setDependencies(
            Collections.singletonList( (Dependency)
                new JDomDependency(
                    new Element( "dependency", "http://maven.apache.org/POM/4.0.0" )
                        .addContent( new Text( UNIX_LS + "        " ) )
                        .addContent( new Element( "groupId", "http://maven.apache.org/POM/4.0.0" )
                            .addContent( "org.apache.commons" ) )
                        .addContent( new Text( UNIX_LS + "        " ) )
                        .addContent( new Element( "artifactId", "http://maven.apache.org/POM/4.0.0" )
                            .addContent( "commons-text" ) )
                        .addContent( new Text( UNIX_LS + "        " ) )
                        .addContent( new Element( "version", "http://maven.apache.org/POM/4.0.0" )
                            .addContent( "1.6" ) )
                        .addContent( new Text( UNIX_LS + "      " ) )
                )
            )
        );
        assertTransformation();
    }

    protected List<Plugin> getPluginsFromModel()
    {
        return jDomModelETL.getModel().getBuild().getPlugins();
    }
}
