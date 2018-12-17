package org.apache.maven.model.jdom.it;

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

import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.junit.Test;

/**
 * Tests transformations of {@code dependencies}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class DependenciesEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void changeDependencyGroupId() throws IOException
    {
        for ( Dependency dependency : getDependenciesFromModel().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getGroupId().equals( "com.coremedia.test" ) )
            {
                dependency.setGroupId( "${project.groupId}" );
            }
        }
        assertTransformation();
    }

    @Test
    public void changeDependencyVersion() throws IOException
    {
        for ( Dependency dependency : getDependenciesFromModel().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getArtifactId().equals( "my-dependency" ) )
            {
                dependency.setVersion( "1.0" );
            }
        }
        assertTransformation();
    }

    @Test
    public void renameDependency() throws IOException
    {
        for ( Dependency dependency : getDependenciesFromModel().toArray( new Dependency[]{} ) )
        {
            if ( dependency.getArtifactId().equals( "my-dependency" ) )
            {
                dependency.setArtifactId( "my-renamed-dependency" );
            }
        }
        assertTransformation();
    }

    protected List<Dependency> getDependenciesFromModel()
    {
        return jDomModelETL.getModel().getDependencies();
    }
}
