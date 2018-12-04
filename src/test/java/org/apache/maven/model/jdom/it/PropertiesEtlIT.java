package org.apache.maven.model.jdom.it;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

/**
 * Tests transformations of {@code properties}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PropertiesEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void addProperty() throws IOException
    {
        jDomModelETL.getModel().addProperty( "property.new", "value-new" );
        assertTransformation();
    }

    @Test
    public void changePropertyValue() throws IOException
    {
        for ( Map.Entry<Object, Object> property : jDomModelETL.getModel().getProperties().entrySet() )
        {
            if ( property.getValue().equals( "1.0-SNAPSHOT" ) )
            {
                property.setValue( "${project.version}" );
            }
        }
        assertTransformation();
    }

    @Test
    public void removeProperty() throws IOException
    {
        jDomModelETL.getModel().getProperties().remove( "property.b" );
        assertTransformation();
    }
}
