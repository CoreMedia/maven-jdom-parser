package org.apache.maven.model.jdom.it;

import java.io.IOException;

import org.apache.maven.model.Parent;
import org.junit.Test;

/**
 * Tests transformations of (root) {@code project} elements.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class ProjectEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void changeArtifactId() throws IOException
    {
        jDomModelETL.getModel().setArtifactId( "my-renamed-test-project" );
        assertTransformation();
    }

    @Test
    public void changeParentVersion() throws IOException
    {
        jDomModelETL.getModel().getParent().setVersion( "20" );
        assertTransformation();
    }

    @Test
    public void changeProjectVersionSimple() throws IOException
    {
        jDomModelETL.getModel().setVersion( "1.2.3.4.5.6.7.8.9-SNAPSHOT" );
        assertTransformation();
    }

    @Test
    public void changeProjectVersionParent() throws IOException
    {
        jDomModelETL.getModel().setVersion( "9.8.7.6.5.4.3.2.1-SNAPSHOT" );
        assertTransformation();
    }

    @Test
    public void removeParent() throws IOException
    {
        jDomModelETL.getModel().setParent( null );
        assertTransformation();
    }

    @Test
    public void setParent() throws IOException
    {
        Parent parent = new Parent();
        parent.setGroupId( "org.apache" );
        parent.setArtifactId( "apache" );
        parent.setVersion( "21" );
        jDomModelETL.getModel().setParent( parent );
        assertTransformation();
    }
}
