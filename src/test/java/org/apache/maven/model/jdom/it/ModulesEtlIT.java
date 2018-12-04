package org.apache.maven.model.jdom.it;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Profile;
import org.junit.Test;

/**
 * Tests transformations of {@code modules}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class ModulesEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void addModule() throws IOException
    {
        jDomModelETL.getModel().addModule( "new-module" );
        assertTransformation();
    }

    @Test
    public void resetModules() throws IOException
    {
        jDomModelETL.getModel().setModules( Arrays.asList( "new-module-1", "new-module-2" ) );
        assertTransformation();
    }

    @Test
    public void resetProfileModules() throws IOException
    {
        List<Profile> profiles = jDomModelETL.getModel().getProfiles();
        for ( Profile profile : profiles )
        {
            switch ( profile.getId() )
            {
                case "profile-1":
                    profile.setModules( Arrays.asList( "new-module-1", "new-module-2", "new-module-3" ) );
                    break;
                case "profile-2":
                    profile.setModules( Collections.singletonList( "new-module-4" ) );
                    break;
            }
        }
        assertTransformation();
    }
}
