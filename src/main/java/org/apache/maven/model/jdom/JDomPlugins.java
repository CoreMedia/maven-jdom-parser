package org.apache.maven.model.jdom;

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.removeChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.resetIndentations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

public class JDomPlugins extends ArrayList<Plugin>
{
    private Element plugins;

    public JDomPlugins( Element plugins )
    {
        super( transformToJDomPluginList( getPluginElements( plugins ) ) );
        this.plugins = plugins;
    }

    private static List<Element> getPluginElements( Element plugins )
    {
        return plugins.getContent( new ElementFilter( "plugin", plugins.getNamespace() ) );
    }

    private static List<JDomPlugin> transformToJDomPluginList( List<Element> pluginElements )
    {
        List<JDomPlugin> jDomPluginList = new ArrayList<>( pluginElements.size() );
        for ( Element pluginElement : pluginElements )
        {
            jDomPluginList.add( new JDomPlugin( pluginElement ) );
        }
        return jDomPluginList;
    }

    @Override
    public boolean add( Plugin plugin )
    {
        Element newElement;
        if ( plugin instanceof JDomPlugin )
        {
            newElement = ( (JDomPlugin) plugin ).getJDomElement().clone();
            plugins.addContent(
                plugins.getContentSize() - 1,
                asList(
                    new Text( "\n" + detectIndentation( plugins ) ),
                    newElement ) );
            resetIndentations( plugins, detectIndentation( plugins ) );
            resetIndentations( newElement, detectIndentation( plugins ) + "  " );
        }
        else
        {
            newElement = insertNewElement( "plugin", plugins );
            JDomPlugin jDomPlugin = new JDomPlugin( newElement );

            jDomPlugin.setGroupId( plugin.getGroupId() );
            jDomPlugin.setArtifactId( plugin.getArtifactId() );
            jDomPlugin.setVersion( plugin.getVersion() );

            List<Dependency> dependencies = plugin.getDependencies();
            if ( !dependencies.isEmpty() )
            {
                jDomPlugin.setDependencies( dependencies );
            }

            if ( plugin.isExtensions() )
            {
                jDomPlugin.setExtensions( true );
            }

            List<PluginExecution> executions = plugin.getExecutions();
            if ( !executions.isEmpty() )
            {
                jDomPlugin.setExecutions( executions );
            }

            if ( !plugin.isInherited() )
            {
                jDomPlugin.setInherited( false );
            }
        }

        return super.add( plugin );
    }

    @Override
    public boolean remove( final Object plugin )
    {
        Plugin removePlugin = (Plugin) plugin;
        for ( Plugin candidate : this )
        {
            if ( candidate.getGroupId().equals( removePlugin.getGroupId() )
                && candidate.getArtifactId().equals( removePlugin.getArtifactId() ) )
            {
                removeChildElement( plugins, ( (JDomPlugin) candidate ).getJDomElement() );
                return super.remove( removePlugin );
            }
        }
        return false;
    }

    @Override
    public boolean addAll( Collection<? extends Plugin> plugins )
    {
        boolean added = false;
        for ( Plugin plugin : plugins )
        {
            added |= this.add( plugin );
        }
        return added;
    }

    @Override
    public boolean addAll( int index, Collection<? extends Plugin> plugins )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll( Collection<?> plugins )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll( Collection<?> plugins )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        while ( size() > 0 )
        {
            remove( 0 );
        }
    }

    @Override
    public Plugin set( int index, Plugin plugin )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add( int index, Plugin plugin )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Plugin remove( int index )
    {
        Plugin plugin = get( index );
        remove( plugin );
        return plugin;
    }

    @Override
    public int lastIndexOf( Object plugin )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Plugin> listIterator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Plugin> listIterator( int index )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Plugin> subList( int fromIndex, int toIndex )
    {
        throw new UnsupportedOperationException();
    }
}
