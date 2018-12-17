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

import org.apache.maven.model.Scm;
import org.junit.Test;

/**
 * Tests transformations of {@code scm}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class ScmEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void addScm() throws IOException
    {
        Scm scm = new Scm();
        scm.setConnection( "scm:git:https://github.com/CoreMedia/maven-jdom-parser.git" );
        scm.setDeveloperConnection( "scm:git:https://github.com/CoreMedia/maven-jdom-parser.git" );
        scm.setTag( "HEAD" );
        scm.setUrl( "https://github.com/CoreMedia/maven-jdom-parser/tree/${project.scm.tag}" );
        jDomModelETL.getModel().setScm( scm );
        assertTransformation();
    }

    @Test
    public void removeScm() throws IOException
    {
        jDomModelETL.getModel().setScm( null );
        assertTransformation();
    }
}
