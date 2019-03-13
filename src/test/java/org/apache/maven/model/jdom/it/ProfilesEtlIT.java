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
    public void addFirstProfile() throws IOException
    {
        Profile profile = new Profile();
        profile.setId( "first-profile" );
        jDomModelETL.getModel().addProfile( profile );
        assertTransformation();
    }

    @Test
    public void addProfile() throws IOException
    {
        Profile profile = new Profile();
        profile.setId( "new-profile-1" );
        jDomModelETL.getModel().addProfile( profile );
        assertTransformation();
    }

    @Test
    public void getEmptyProfiles() throws IOException
    {
        // This test seems trivial, but it ensures that no empty <profiles></profiles>
        // node is added to the POMs just because getProfiles() is called.
        jDomModelETL.getModel().getProfiles();
        assertTransformation();
    }

    @Test
    public void removeLastProfile() throws IOException
    {
        Profile profile = new Profile();
        profile.setId( "last-profile" );
        jDomModelETL.getModel().removeProfile( profile );
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
