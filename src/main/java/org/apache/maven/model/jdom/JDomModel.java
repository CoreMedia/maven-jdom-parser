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

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Scm;
import org.apache.maven.model.jdom.util.JDomUtils;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 * JDom implementation of poms PROJECT element
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomModel extends Model implements MavenCoordinate
{
    private final Element project;

    private final JDomModelBase modelBase;
    private final JDomMavenCoordinate coordinate;

    public JDomModel( Document document )
    {
        this( document.getRootElement() );
    }

    public JDomModel( Element project )
    {
        this.project = project;
        this.modelBase = new JDomModelBase( project );
        this.coordinate = new JDomMavenCoordinate( project );
    }

    @Override
    public String getArtifactId()
    {
        return coordinate.getArtifactId();
    }

    @Override
    public void setArtifactId( String artifactId )
    {
        coordinate.setArtifactId( artifactId );
    }

    @Override
    public Build getBuild()
    {
        return modelBase.getBuild();
    }

    @Override
    public List<Dependency> getDependencies()
    {
        return modelBase.getDependencies();
    }

    @Override
    public DependencyManagement getDependencyManagement()
    {
        return modelBase.getDependencyManagement();
    }

    @Override
    public String getGroupId()
    {
        return coordinate.getGroupId();
    }

    @Override
    public void setGroupId( String groupId )
    {
        coordinate.setGroupId( groupId );
    }

    @Override
    public Parent getParent()
    {
        Element elm = getChildElement( "parent", project );
        if ( elm == null )
        {
            return null;
        }
        else
        {
            // this way parent setters change DOM tree immediately
            return new JDomParent( elm );
        }
    }

    @Override
    public void setParent( Parent parent )
    {
        if ( parent == null )
        {
            JDomUtils.rewriteElement( "parent", null, project, project.getNamespace() );
        }
        else
        {
            boolean containsRelativePath = false;

            Parent jdomParent = getParent();
            if ( jdomParent == null )
            {
                Element parentRoot = insertNewElement( "parent", project );
                jdomParent = new JDomParent( parentRoot );
            }
            else
            {
                containsRelativePath = jdomParent.getRelativePath() != null;
            }

            // Write current values to JDom tree
            jdomParent.setGroupId( parent.getGroupId() );
            jdomParent.setArtifactId( parent.getArtifactId() );
            jdomParent.setVersion( parent.getVersion() );

            String relativePath = parent.getRelativePath();
            if ( relativePath != null && !parent.getRelativePath().equals( "../pom.xml" ) || containsRelativePath )
            {
                jdomParent.setRelativePath( relativePath );
            }

        }
    }

    @Override
    public List<String> getModules()
    {
        return modelBase.getModules();
    }

    @Override
    public void setModules( List<String> modules )
    {
        modelBase.setModules( modules );
    }

    @Override
    public List<Profile> getProfiles()
    {
        Element profilesElm = project.getChild( "profiles", project.getNamespace() );
        if ( profilesElm == null )
        {
            return Collections.emptyList();
        }
        else
        {
            List<Element> profileElms = profilesElm.getChildren( "profile", project.getNamespace() );

            List<Profile> profiles = new ArrayList<>( profileElms.size() );

            for ( Element profileElm : profileElms )
            {
                profiles.add( new JDomProfile( profileElm ) );
            }

            return profiles;
        }
    }


    @Override
    public Properties getProperties()
    {
        Element properties = project.getChild( "properties", project.getNamespace() );

        if ( properties == null )
        {
            return null;
        }
        else
        {
            return new JDomProperties( properties );
        }
    }

    @Override
    public Reporting getReporting()
    {
        Element reporting = project.getChild( "reporting", project.getNamespace() );

        if ( reporting == null )
        {
            return null;
        }
        else
        {
            return new JDomReporting( reporting );
        }
    }

    @Override
    public void setScm( Scm scm )
    {
        if ( scm == null )
        {
            JDomUtils.rewriteElement( "scm", null, project, project.getNamespace() );
        }
        else
        {
            Scm jdomScm = getScm();
            if ( jdomScm == null )
            {
                Element scmRoot = insertNewElement( "scm", project );
                jdomScm = new JDomScm( scmRoot );
            }

            // Write current values to JDom tree
            jdomScm.setConnection( scm.getConnection() );
            jdomScm.setDeveloperConnection( scm.getDeveloperConnection() );
            jdomScm.setTag( scm.getTag() );
            jdomScm.setUrl( scm.getUrl() );
        }
    }

    @Override
    public Scm getScm()
    {
        Element elm = project.getChild( "scm", project.getNamespace() );
        if ( elm == null )
        {
            return null;
        }
        else
        {
            // this way scm setters change DOM tree immediately
            return new JDomScm( elm );
        }
    }

    @Override
    public String getVersion()
    {
        return coordinate.getVersion();
    }

    @Override
    public void setVersion( String version )
    {
        String projectVersion = coordinate.getVersion();
        if ( projectVersion != null )
        {
            coordinate.setVersion( version );
        }
        else
        {
            Parent parent = getParent();
            if ( parent == null || !version.equals( parent.getVersion() ) )
            {
                coordinate.setVersion( version );
            }
        }
    }
}
