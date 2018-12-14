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

import java.util.List;

import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Profile;
import org.jdom2.Element;

/**
 * JDom implementation of poms PROFILE element
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomProfile
    extends Profile
{
    private Element profile;

    private final JDomModelBase modelBase;

    public JDomProfile( Element profile )
    {
        this.profile = profile;
        this.modelBase = new JDomModelBase( profile ) ;
    }

    @Override
    public BuildBase getBuild()
    {
        return modelBase.getBuild();
    }

    @Override
    public List<Dependency> getDependencies()
    {
        return modelBase.getDependencies();
    }

    @Override
    public DependencyManagement getDependencyManagement()
    {
        return modelBase.getDependencyManagement();
    }

    @Override
    public void setDependencyManagement( DependencyManagement dependencyManagement )
    {
        modelBase.setDependencyManagement( dependencyManagement );
    }

    @Override
    public String getId()
    {
        return getChildElementTextTrim( "id", profile );
    }

    @Override
    public List<String> getModules()
    {
        return modelBase.getModules();
    }

    @Override
    public void setModules( List<String> modules )
    {
        modelBase.setModules( modules );
    }
}
