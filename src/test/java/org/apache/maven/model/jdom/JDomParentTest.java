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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class JDomParentTest
{
    private SAXBuilder builder = new SAXBuilder();

    @Test
    public void testGetArtifactId() throws Exception
    {
        String content = "<parent></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( getChildElementTextTrim( "artifactId", parentElm ) );

        content = "<parent><artifactId>ARTIFACTID</artifactId></parent>";
        parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "ARTIFACTID", new JDomParent( parentElm ).getArtifactId() );
    }

    @Test
    public void testGetGroupId() throws Exception
    {
        String content = "<parent></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( getChildElementTextTrim( "groupId", parentElm ) );

        content = "<parent><groupId>GROUPID</groupId></parent>";
        parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "GROUPID", new JDomParent( parentElm ).getGroupId() );
    }

    @Test
    public void testGetRelativePath() throws Exception
    {
        String content = "<parent></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( getChildElementTextTrim( "relativePath", parentElm ) );

        content = "<parent><relativePath>RELATIVEPATH</relativePath></parent>";
        parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "RELATIVEPATH", new JDomParent( parentElm ).getRelativePath() );
    }

    @Test
    public void testGetVersion() throws Exception
    {
        String content = "<parent></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( getChildElementTextTrim( "version", parentElm ) );

        content = "<parent><version>VERSION</version></parent>";
        parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "VERSION", new JDomParent( parentElm ).getVersion() );
    }

    @Test
    public void testSetArtifactId() throws Exception
    {
        String content = "<parent><artifactId>OLD_ARTIFACTID</artifactId></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomParent( parentElm ).setArtifactId( "NEW_ARTIFACTID" );
        assertEquals( "NEW_ARTIFACTID", getChildElementTextTrim( "artifactId", parentElm ) );
    }

    @Test
    public void testSetGroupId() throws Exception
    {
        String content = "<parent><groupId>OLD_GROUPID</groupId></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomParent( parentElm ).setGroupId( "NEW_GROUPID" );
        assertEquals( "NEW_GROUPID", getChildElementTextTrim( "groupId", parentElm ) );
    }

    @Test
    public void testSetRelativePath() throws Exception
    {
        String content = "<parent><relativePath>OLD_RELATIVEPATH</relativePath></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomParent( parentElm ).setRelativePath( "NEW_RELATIVEPATH" );
        assertEquals( "NEW_RELATIVEPATH", getChildElementTextTrim( "relativePath", parentElm ) );
    }

    @Test
    public void testSetVersionString() throws Exception
    {
        String content = "<parent></parent>";
        Element parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( getChildElementTextTrim( "version", parentElm ) );

        content = "<parent><version>VERSION</version></parent>";
        parentElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "VERSION", new JDomParent( parentElm ).getVersion() );

    }
}
