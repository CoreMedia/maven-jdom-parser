package org.apache.maven.model.jdom.it;

import java.io.IOException;

import org.apache.maven.model.Profile;
import org.junit.Test;

/**
 * Tests transformations of {@code profiles}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class ProfilesEtlIT extends AbstractJDomModelEtlIT
{
    @Test
    public void addProfile() throws IOException
    {
        Profile profile = new Profile();
        profile.setId( "new-profile-1" );
        jDomModelETL.getModel().addProfile( profile );
        assertTransformation();
    }

    @Test
    public void removeProfile() throws IOException
    {
        Profile profile = new Profile();
        profile.setId( "profile-2" );
        jDomModelETL.getModel().removeProfile( profile );
        assertTransformation();
    }
}
