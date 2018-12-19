package org.apache.maven.model.jdom;

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.removeChildElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

/**
 * JDOM implementation of POMs {@code modules} element.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomModules extends ArrayList<String>
{
    private Element modules;

    public JDomModules( Element modules )
    {
        super( transformToElementTextList( getModuleElements( modules ) ) );
        this.modules = modules;
    }

    private static List<Element> getModuleElements( Element modules )
    {
        return modules.getContent( new ElementFilter( modules.getNamespace() ) );
    }

    private static List<String> transformToElementTextList( List<Element> elements )
    {
        List<String> elementTextList = new ArrayList<>( elements.size() );
        for ( Element element : elements )
        {
            elementTextList.add( element.getTextTrim() );
        }
        return elementTextList;
    }

    @Override
    public boolean add( String module )
    {
        Element newModule = new Element( "module", modules.getNamespace() );
        newModule.setText( module );

        modules.addContent(
            modules.getContentSize() - 1,
            asList(
                new Text( "\n" + detectIndentation( modules ) ),
                newModule ) );
        return super.add( module );
    }

    @Override
    public boolean remove( final Object module )
    {
        List<Element> removeElements = modules.getContent( new ElementFilter()
        {
            @Override
            public Element filter( Object content )
            {
                Element element = super.filter( content );
                return element == null || !module.equals( element.getTextTrim() ) ? null : element;
            }
        } );

        for ( Element removeElement : removeElements )
        {
            removeChildElement( modules, removeElement );
        }

        return super.remove( module );
    }

    @Override
    public boolean addAll( Collection<? extends String> modules )
    {
        boolean added = false;
        for ( String module : modules )
        {
            added |= this.add( module );
        }
        return added;
    }

    @Override
    public boolean addAll( int index, Collection<? extends String> modules )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll( Collection<?> modules )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll( Collection<?> modules )
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
    public String set( int index, String module )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add( int index, String module )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove( int index )
    {
        String module = get( index );
        remove( module );
        return module;
    }

    @Override
    public int lastIndexOf( Object module )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<String> listIterator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<String> listIterator( int index )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList( int fromIndex, int toIndex )
    {
        throw new UnsupportedOperationException();
    }
}