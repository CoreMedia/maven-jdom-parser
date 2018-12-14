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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringReader;
import java.util.Arrays;

import org.apache.maven.model.Dependency;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class JDomDependencyManagementTest
{
    private SAXBuilder builder = new SAXBuilder();

    @Test
    public void testGetDependencies() throws Exception
    {
        String content = "<dependencyManamgement></dependencyManamgement>";
        Document document = builder.build( new StringReader( content ) );
        assertNotNull( new JDomDependencyManagement( document.getRootElement() ).getDependencies() );
        assertEquals( 0, new JDomDependencyManagement( document.getRootElement() ).getDependencies().size() );

        content = "<dependencyManamgement><dependencies/></dependencyManamgement>";
        document = builder.build( new StringReader( content ) );
        assertEquals( 0, new JDomDependencyManagement( document.getRootElement() ).getDependencies().size() );

        content = "<dependencyManamgement><dependencies><dependency/></dependencies></dependencyManamgement>";
        document = builder.build( new StringReader( content ) );
        assertEquals( 1, new JDomDependencyManagement( document.getRootElement() ).getDependencies().size() );
    }

    @Test
    public void testAddDependency() throws Exception
    {
        Dependency addDependency = new Dependency();
        addDependency.setGroupId( "a" );
        addDependency.setArtifactId( "b" );

        String content = "<dependencyManamgement><dependencies> </dependencies></dependencyManamgement>";
        Document document = builder.build( new StringReader( content ) );
        JDomDependencyManagement dependencyManagement = new JDomDependencyManagement( document.getRootElement() );
        assertEquals( 0, dependencyManagement.getDependencies().size() );
        dependencyManagement.addDependency( addDependency );
        assertEquals( 1, dependencyManagement.getDependencies().size() );
    }

    @Test
    public void testRemoveJDomDependency() throws Exception
    {
        String contentDep = "<dependency><groupId>a</groupId><artifactId>b</artifactId></dependency>";
        String contentDepMgmt = "<dependencyManamgement><dependencies>" + contentDep + "</dependencies></dependencyManamgement>";
        Document documentDep = builder.build( new StringReader( contentDep ) );
        Document documentDepMgmt = builder.build( new StringReader( contentDepMgmt ) );
        JDomDependencyManagement dependencyManagement = new JDomDependencyManagement( documentDepMgmt.getRootElement() );
        assertEquals( 1, dependencyManagement.getDependencies().size() );
        dependencyManagement.removeDependency( new JDomDependency( documentDep.getRootElement() ) );
        assertEquals( 0, dependencyManagement.getDependencies().size() );
    }

    @Test
    public void testRemoveModelDependency() throws Exception
    {
        Dependency removeDependency = new Dependency();
        removeDependency.setGroupId( "a" );
        removeDependency.setArtifactId( "b" );

        String contentDepMgmt = "<dependencyManamgement><dependencies><dependency><groupId>a</groupId><artifactId>b</artifactId></dependency></dependencies></dependencyManamgement>";
        Document documentDepMgmt = builder.build( new StringReader( contentDepMgmt ) );
        JDomDependencyManagement dependencyManagement = new JDomDependencyManagement( documentDepMgmt.getRootElement() );
        assertEquals( 1, dependencyManagement.getDependencies().size() );
        dependencyManagement.removeDependency( removeDependency );
        assertEquals( 0, dependencyManagement.getDependencies().size() );
    }

    // All other methods throw UnsupportedOperationException

    @Test
    public void testSetDependenciesListOfDependency() throws Exception
    {
        Dependency addDependency1 = new Dependency();
        addDependency1.setGroupId( "a" );
        addDependency1.setArtifactId( "b" );
        Dependency addDependency2 = new Dependency();
        addDependency2.setGroupId( "c" );
        addDependency2.setArtifactId( "d" );

        String content = "<dependencyManamgement> </dependencyManamgement>";
        Document document = builder.build( new StringReader( content ) );
        JDomDependencyManagement dependencyManagement = new JDomDependencyManagement( document.getRootElement() );
        assertEquals( 0, dependencyManagement.getDependencies().size() );
        dependencyManagement.setDependencies( Arrays.asList( addDependency1, addDependency2 ) );
        assertEquals( 2, dependencyManagement.getDependencies().size() );
    }
}
