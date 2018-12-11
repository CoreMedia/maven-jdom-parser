package org.apache.maven.model.jdom.it;

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
