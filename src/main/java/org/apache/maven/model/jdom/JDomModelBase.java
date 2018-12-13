package org.apache.maven.model.jdom;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.toElementTextList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.jdom.util.JDomUtils;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

/**
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomModelBase
{
    private final Element modelBase;

    public JDomModelBase( Element modelBase )
    {
        this.modelBase = modelBase;
    }

    public Build getBuild()
    {
        Element elm = modelBase.getChild( "build", modelBase.getNamespace() );
        if ( elm == null )
        {
            return null;
        }
        else
        {
            // this way build setters change DOM tree immediately
            return new JDomBuild( elm );
        }
    }

    public List<Dependency> getDependencies()
    {
        Element dependenciesElm = modelBase.getChild( "dependencies", modelBase.getNamespace() );
        if ( dependenciesElm == null )
        {
            return Collections.emptyList();
        }
        else
        {
            List<Element> dependencyElms = dependenciesElm.getChildren( "dependency", modelBase.getNamespace() );

            List<Dependency> dependencies = new ArrayList<>( dependencyElms.size() );

            for ( Element dependencyElm : dependencyElms )
            {
                dependencies.add( new JDomDependency( dependencyElm ) );
            }

            return dependencies;
        }
    }

    public DependencyManagement getDependencyManagement()
    {
        Element elm = modelBase.getChild( "dependencyManagement", modelBase.getNamespace() );
        if ( elm == null )
        {
            return null;
        }
        else
        {
            // this way build setters change DOM tree immediately
            return new JDomDependencyManagement( elm );
        }
    }

    public List<String> getModules()
    {
        Element modulesElement = getChildElement( "modules", modelBase );
        if ( modulesElement == null )
        {
            return Collections.emptyList();
        }
        else
        {
            return new JDomModules( modulesElement );
        }
    }

    public void setModules( List<String> modules )
    {
        JDomUtils.rewriteElement( "modules", null, modelBase, modelBase.getNamespace() );
        new JDomModules( insertNewElement( "modules", modelBase ) ).addAll( modules );
    }

    private class JDomModules extends ArrayList<String>
    {
        private Element modules;

        private JDomModules( Element modules )
        {
            super( toElementTextList( modelBase.getContent( new ElementFilter( modelBase.getNamespace() ) ) ) );
            this.modules = modules;
        }

        @Override
        public boolean add( String module )
        {
            Element newModule = new Element( "module", modules.getNamespace() );
            newModule.setText( module );

            modules.addContent(
                modules.getContentSize() - 1,
                asList(
                    new Text( "\n  " + detectIndentation( modelBase ) ),
                    newModule ) );
            return super.add( module );
        }

        @Override
        public boolean remove( Object module )
        {
            throw new UnsupportedOperationException();
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
            throw new UnsupportedOperationException();
        }

        @Override
        public String get( int index )
        {
            throw new UnsupportedOperationException();
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
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf( Object module )
        {
            throw new UnsupportedOperationException();
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
}
