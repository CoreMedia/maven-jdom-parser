package org.apache.maven.model.jdom.it;

import static org.apache.maven.model.jdom.etl.ModelETLRequest.UNIX_LS;

import java.io.IOException;
import java.util.Collections;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginContainer;
import org.apache.maven.model.jdom.JDomConfiguration;
import org.apache.maven.model.jdom.JDomDependency;
import org.apache.maven.model.jdom.JDomPlugin;
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
    public void addPlugin()
    {
        Plugin plugin = new Plugin();
        plugin.setGroupId( "org.apache.maven.plugins" );
        plugin.setArtifactId( "maven-antrun-plugin" );
        plugin.setVersion( "1.8" );
        getPluginPluginContainer().addPlugin( plugin );
    }

    @Test
    public void changeConfigurationValue() throws IOException
    {
        ( (Xpp3Dom) getPluginPluginContainer().getPlugins().get( 0 ).getConfiguration() ).getChild( "skipDeploy" ).setValue( "true" );
        assertTransformation();
    }

    @Test
    public void changePluginVersion() throws IOException
    {
        for ( Plugin plugin : getPluginPluginContainer().getPlugins().toArray( new Plugin[]{} ) )
        {
            if ( plugin.getArtifactId().equals( "maven-site-plugin" ) )
            {
                plugin.setVersion( "3.7.1" );
            }
        }
        assertTransformation();
    }

    @Test
    public void removePlugin()
    {
        Plugin plugin = new Plugin();
        plugin.setGroupId( "org.apache.maven.plugins" );
        plugin.setArtifactId( "maven-site-plugin" );
        getPluginPluginContainer().removePlugin( plugin );
    }

    @Test
    public void resetJDomConfiguration() throws IOException
    {
        JDomConfiguration jDomConfiguration = new JDomConfiguration(
            new Element( "configuration", "http://maven.apache.org/POM/4.0.0" )
                .addContent( new Text( UNIX_LS + "            " ) )
                .addContent( new Element( "skipDeploy", "http://maven.apache.org/POM/4.0.0" )
                    .addContent( "true" ) )
                .addContent( new Text( UNIX_LS + "          " ) ) );
        new JDomPlugin( new Element( "plugin", "http://maven.apache.org/POM/4.0.0" )
            .addContent( jDomConfiguration.getJDomElement() ) );
        getPluginPluginContainer().getPlugins().get( 0 ).setConfiguration( jDomConfiguration );
        assertTransformation();
    }

    @Test
    public void setJDomConfiguration() throws IOException
    {
        getPluginPluginContainer().getPlugins().get( 0 ).setConfiguration(
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
        getPluginPluginContainer().getPlugins().get( 0 ).setDependencies(
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

    protected PluginContainer getPluginPluginContainer()
    {
        return jDomModelETL.getModel().getBuild();
    }
}
