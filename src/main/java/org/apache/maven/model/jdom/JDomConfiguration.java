package org.apache.maven.model.jdom;

/*
 * Copyright 2018 CoreMedia AG, Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.maven.model.jdom.util.JDomUtils.rewriteValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;

/**
 * JDOM implementation of POM plugins {@code configuration} element.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomConfiguration extends Xpp3Dom
{
    private Element configuration;

    private List<JDomConfiguration> children;

    public JDomConfiguration( Element configuration )
    {
        super( configuration.getName() );
        this.configuration = configuration;
        this.children = getChildren( configuration );
    }

    private static List<JDomConfiguration> getChildren( Element configuration )
    {
        List<Element> childElements = configuration.getContent( new ElementFilter() );
        List<JDomConfiguration> children = new ArrayList<>( childElements.size() );
        for ( Element childElement : childElements )
        {
            children.add( new JDomConfiguration( childElement ) );
        }
        return children;
    }

    @Override
    public String getValue()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue( String value )
    {
        rewriteValue( configuration, value );
    }

    @Override
    public String[] getAttributeNames()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAttribute( String name )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute( String name, String value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Xpp3Dom getChild( int i )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Xpp3Dom getChild( String name )
    {
        for ( JDomConfiguration child : children )
        {
            if ( child.getName().equals( name ) )
            {
                return child;
            }
        }
        return null;
    }

    @Override
    public void addChild( Xpp3Dom xpp3Dom )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Xpp3Dom[] getChildren()
    {
        return children.toArray( new JDomConfiguration[]{} );
    }

    @Override
    public Xpp3Dom[] getChildren( String name )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getChildCount()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeChild( int i )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Xpp3Dom getParent()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParent( Xpp3Dom parent )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeToSerializer( String namespace, XmlSerializer serializer ) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toUnescapedString()
    {
        throw new UnsupportedOperationException();
    }

    public Element getJDomElement()
    {
        return configuration;
    }
}
