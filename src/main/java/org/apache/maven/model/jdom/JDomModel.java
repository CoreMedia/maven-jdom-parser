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
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
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
    public void setBuild( Build build )
    {
        modelBase.setBuild( build );
    }

    @Override
    public CiManagement getCiManagement()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCiManagement( CiManagement ciManagement )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Contributor> getContributors()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContributors( List<Contributor> contributors )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Dependency> getDependencies()
    {
        return modelBase.getDependencies();
    }

    @Override
    public void setDependencies( List<Dependency> dependencies )
    {
        modelBase.setDependencies( dependencies );
    }

    @Override
    public DependencyManagement getDependencyManagement()
    {
        return modelBase.getDependencyManagement();
    }

    @Override
    public void setDependencyManagement( DependencyManagement dependencyManagement )
    {
        modelBase.setDependencyManagement( dependencyManagement );
    }

    @Override
    public String getDescription()
    {
        return getChildElementTextTrim( "description", project );
    }

    @Override
    public void setDescription( String description )
    {
        rewriteElement( "description", description, project, project.getNamespace() );
    }

    @Override
    public List<Developer> getDevelopers()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDevelopers( List<Developer> developers )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public DistributionManagement getDistributionManagement()
    {
        return modelBase.getDistributionManagement();
    }

    @Override
    public void setDistributionManagement( DistributionManagement distributionManagement )
    {
        modelBase.setDistributionManagement( distributionManagement );
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
    public String getInceptionYear()
    {
        return getChildElementTextTrim( "inceptionYear", project );
    }

    @Override
    public void setInceptionYear( String inceptionYear )
    {
        rewriteElement( "inceptionYear", inceptionYear, project, project.getNamespace() );
    }

    @Override
    public IssueManagement getIssueManagement()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIssueManagement( IssueManagement issueManagement )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<License> getLicenses()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLicenses( List<License> licenses )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<MailingList> getMailingLists()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMailingLists( List<MailingList> mailingLists )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getModelVersion()
    {
        return getChildElementTextTrim( "modelVersion", project );
    }

    @Override
    public void setModelVersion( String modelVersion )
    {
        rewriteElement( "modelVersion", modelVersion, project, project.getNamespace() );
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
    public String getName()
    {
        return getChildElementTextTrim( "name", project );
    }

    @Override
    public void setName( String name )
    {
        rewriteElement( "name", name, project, project.getNamespace() );
    }

    @Override
    public Organization getOrganization()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOrganization( Organization organization )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPackaging()
    {
        return getChildElementTextTrim( "packaging", project );
    }

    @Override
    public void setPackaging( String packaging )
    {
        rewriteElement( "packaging", packaging, project, project.getNamespace() );
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
    public List<Repository> getPluginRepositories()
    {
        return modelBase.getPluginRepositories();
    }

    @Override
    public void setPluginRepositories( List<Repository> pluginRepositories )
    {
        modelBase.setPluginRepositories( pluginRepositories );
    }

    @Override
    public Prerequisites getPrerequisites()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrerequisites( Prerequisites prerequisites )
    {
        throw new UnsupportedOperationException();
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
            return new JDomProfiles( profilesElm );
        }
    }

    @Override
    public void setProfiles( List<Profile> profiles )
    {
        if ( profiles == null )
        {
            JDomUtils.rewriteElement( "profiles", null, project, project.getNamespace() );
        }
        else
        {
            new JDomProfiles( insertNewElement( "profiles", project ) ).addAll( profiles );
        }
    }

    @Override
    public Properties getProperties()
    {
        return modelBase.getProperties();
    }

    @Override
    public void setProperties( Properties properties )
    {
        modelBase.setProperties( properties );
    }

    @Override
    public Reporting getReporting()
    {
        return modelBase.getReporting();
    }

    @Override
    public void setReporting( Reporting reporting )
    {
        modelBase.setReporting( reporting );
    }

    @Override
    public List<Repository> getRepositories()
    {
        return modelBase.getRepositories();
    }

    @Override
    public void setRepositories( List<Repository> repositories )
    {
        modelBase.setRepositories( repositories );
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
    public String getUrl()
    {
        return getChildElementTextTrim( "url", project );
    }

    @Override
    public void setUrl( String url )
    {
        rewriteElement( "url", url, project, project.getNamespace() );
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
