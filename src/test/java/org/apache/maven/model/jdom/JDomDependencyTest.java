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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class JDomDependencyTest
{
    private SAXBuilder builder = new SAXBuilder();

    @Test
    public void testIsOptional() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertFalse( new JDomDependency( dependencyElm ).isOptional() );

        content = "<dependency><optional>true</optional></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertTrue( new JDomDependency( dependencyElm ).isOptional() );
    }

    @Test
    public void testSetOptional() throws Exception
    {
        String content = "<dependency><optional>false</optional></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setOptional( true );
        assertEquals( "true", getChildElementTextTrim( "optional", dependencyElm ) );

        content = "<dependency></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setOptional( true );
        assertEquals( "true", getChildElementTextTrim( "optional", dependencyElm ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testAddExclusion()
    {
        new JDomDependency( null ).addExclusion( null );
    }

    @Test
    public void testGetArtifactId() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getArtifactId() );

        content = "<dependency><artifactId>ARTIFACTID</artifactId></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "ARTIFACTID", new JDomDependency( dependencyElm ).getArtifactId() );
    }

    @Test
    public void testGetClassifier() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getClassifier() );

        content = "<dependency><classifier>CLASSIFIER</classifier></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "CLASSIFIER", new JDomDependency( dependencyElm ).getClassifier() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetExclusions()
    {
        new JDomDependency( null ).getExclusions();
    }

    @Test
    public void testGetGroupId() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getGroupId() );

        content = "<dependency><groupId>GROUPID</groupId></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "GROUPID", new JDomDependency( dependencyElm ).getGroupId() );
    }

    @Test
    public void testGetScope() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getScope() );

        content = "<dependency><scope>test</scope></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "test", new JDomDependency( dependencyElm ).getScope() );
    }

    @Test
    public void testGetSystemPath() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getSystemPath() );

        content = "<dependency><systemPath>SYSTEMPATH</systemPath></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "SYSTEMPATH", new JDomDependency( dependencyElm ).getSystemPath() );
    }

    @Test
    public void testGetType() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getType() );

        content = "<dependency><type>TYPE</type></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "TYPE", new JDomDependency( dependencyElm ).getType() );
    }

    @Test
    public void testGetVersion() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getVersion() );

        content = "<dependency><version>VERSION</version></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "VERSION", new JDomDependency( dependencyElm ).getVersion() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testRemoveExclusion()
    {
        new JDomDependency( null ).removeExclusion( null );
    }

    @Test
    public void testSetArtifactIdString() throws Exception
    {
        String content = "<dependency><artifactId>OLD_ARTIFACTID</artifactId></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setArtifactId( "NEW_ARTIFACTID" );
        assertEquals( "NEW_ARTIFACTID", getChildElementTextTrim( "artifactId", dependencyElm ) );
    }

    @Test
    public void testSetClassifierString() throws Exception
    {
        String content = "<dependency><classifier>OLD_CLASSIFIER</classifier></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setClassifier( "NEW_CLASSIFIER" );
        assertEquals( "NEW_CLASSIFIER", getChildElementTextTrim( "classifier", dependencyElm ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetExclusions()
    {
        new JDomDependency( null ).setExclusions( null );
    }

    @Test
    public void testSetGroupIdString() throws Exception
    {
        String content = "<dependency><groupId>OLD_GROUPID</groupId></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setGroupId( "NEW_GROUPID" );
        assertEquals( "NEW_GROUPID", getChildElementTextTrim( "groupId", dependencyElm ) );
    }

    @Test
    public void testSetScopeString() throws Exception
    {
        String content = "<dependency><scope>OLD_SCOPE</scope></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setScope( "NEW_SCOPE" );
        assertEquals( "NEW_SCOPE", getChildElementTextTrim( "scope", dependencyElm ) );
    }

    @Test
    public void testSetSystemPathString() throws Exception
    {
        String content = "<dependency><systemPath>OLD_SYSTEMPATH</systemPath></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setSystemPath( "NEW_SYSTEMPATH" );
        assertEquals( "NEW_SYSTEMPATH", getChildElementTextTrim( "systemPath", dependencyElm ) );
    }

    @Test
    public void testSetTypeString() throws Exception
    {
        String content = "<dependency><type>OLD_TYPE</type></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setType( "NEW_TYPE" );
        assertEquals( "NEW_TYPE", getChildElementTextTrim( "type", dependencyElm ) );
    }

    @Test
    public void testSetVersionString() throws Exception
    {
        String content = "<dependency><version>OLD_VERSION</version></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setVersion( "NEW_VERSION" );
        assertEquals( "NEW_VERSION", getChildElementTextTrim( "version", dependencyElm ) );
    }

    @Test
    public void testGetName()
    {
        assertEquals( "dependency", new JDomDependency( null ).getName() );
    }
}
