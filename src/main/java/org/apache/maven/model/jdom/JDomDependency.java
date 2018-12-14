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

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;
import static org.codehaus.plexus.util.StringUtils.defaultString;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.jdom2.Element;

/**
 * JDom implementation of poms DEPENDENCY element
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomDependency extends Dependency implements MavenCoordinate
{
    private Element dependency;

    private final MavenCoordinate coordinate;

    public JDomDependency( Element dependency )
    {
        this.dependency = dependency;
        this.coordinate = new JDomMavenCoordinate( dependency );
    }

    @Override
    public void addExclusion( Exclusion exclusion )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getArtifactId()
    {
        return coordinate.getArtifactId();
    }

    @Override
    public String getClassifier()
    {
        return getChildElementTextTrim( "classifier", dependency );
    }

    @Override
    public List<Exclusion> getExclusions()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getGroupId()
    {
        return coordinate.getGroupId();
    }

    @Override
    public String getScope()
    {
        return getChildElementTextTrim( "scope", dependency );
    }

    @Override
    public String getSystemPath()
    {
        return getChildElementTextTrim( "systemPath", dependency );
    }

    @Override
    public String getType()
    {
        return getChildElementTextTrim( "type", dependency );
    }

    @Override
    public String getVersion()
    {
        return coordinate.getVersion();
    }

    @Override
    public boolean isOptional()
    {
        return Boolean.parseBoolean( getChildElementTextTrim( "optional", dependency ) );
    }

    @Override
    public void removeExclusion( Exclusion exclusion )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArtifactId( String artifactId )
    {
        coordinate.setArtifactId( artifactId );
    }

    @Override
    public void setClassifier( String classifier )
    {
        rewriteElement( "classifier", classifier, dependency, dependency.getNamespace() );
    }

    @Override
    public void setExclusions( List<Exclusion> exclusions )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGroupId( String groupId )
    {
        coordinate.setGroupId( groupId );
    }

    @Override
    public void setOptional( boolean optional )
    {
        rewriteElement( "optional", Boolean.toString( optional ), dependency, dependency.getNamespace() );
    }

    @Override
    public void setScope( String scope )
    {
        rewriteElement( "scope", scope, dependency, dependency.getNamespace() );
    }

    @Override
    public void setSystemPath( String systemPath )
    {
        rewriteElement( "systemPath", systemPath, dependency, dependency.getNamespace() );
    }

    @Override
    public void setType( String type )
    {
        rewriteElement( "type", type, dependency, dependency.getNamespace() );
    }

    @Override
    public void setVersion( String version )
    {
        coordinate.setVersion( version );
    }

    @Override
    public String getName()
    {
        return "dependency";
    }

    public Element getJDomElement()
    {
        return dependency;
    }
}
