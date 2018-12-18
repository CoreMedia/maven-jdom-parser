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

import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteValue;

import org.jdom2.Element;
import org.jdom2.Text;

/**
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomMavenCoordinate implements MavenCoordinate
{
    private final Element element;

    JDomMavenCoordinate( Element elm )
    {
        this.element = elm;
    }

    @Override
    public String getArtifactId()
    {
        return getChildElementTextTrim( "artifactId", element );
    }

    @Override
    public void setArtifactId( String artifactId )
    {
        rewriteElement( "artifactId", artifactId, element, element.getNamespace() );
    }

    @Override
    public String getGroupId()
    {
        return getChildElementTextTrim( "groupId", element );
    }

    @Override
    public void setGroupId( String groupId )
    {
        rewriteElement( "groupId", groupId, element, element.getNamespace() );
    }

    @Override
    public String getVersion()
    {
        return getChildElementTextTrim( "version", element );
    }

    @Override
    public void setVersion( String version )
    {
        Element versionElement = getChildElement( "version", element );
        if ( versionElement == null )
        {
            // This 'if' branch should only be executed when the project version is inherited from the parent but now
            // is changed without having changed the parent version. In this case, the version cannot be inherited
            // anymore and thus the project version element must be added.

            versionElement = new Element( "version", element.getNamespace() );
            versionElement.setText( version );

            // Add the new version element after the artifactId.
            int indexArtifactId = element.indexOf( element.getChild( "artifactId", element.getNamespace() ) );

            // Linebreak and indentation are (tried to be copied) from the existing XML structure.
            String indent = detectIndentation( element );
            if ( indent != null )
            {
                element.addContent( ++indexArtifactId, new Text( "\n" + indent ) );
            }

            element.addContent( ++indexArtifactId, versionElement );
        }
        else
        {
            rewriteValue( versionElement, version );
        }
    }

    @Override
    public String getName()
    {
        return element.getName();
    }
}
