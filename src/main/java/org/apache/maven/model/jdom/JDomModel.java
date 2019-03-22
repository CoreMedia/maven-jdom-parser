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

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Scm;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;
import java.util.Properties;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ARTIFACT_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_BUILD;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DESCRIPTION;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_GROUP_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_INCEPTION_YEAR;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_MODEL_VERSION;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_NAME;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PACKAGING;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PARENT;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROFILES;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SCM;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_URL;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_VERSION;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDOM implementation of the {@link Model} class. It holds the child elements of the Maven POMs root ({@code project})
 * element that are not also definable in a {@code profile}.
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomModel extends Model implements JDomBacked {

  private static final long serialVersionUID = -1392976348805070330L;

  private final Element jdomElement;

  private final JDomModelBase modelBase;

  public JDomModel(Document document) {
    this(document.getRootElement());
  }

  @SuppressWarnings("WeakerAccess")
  public JDomModel(Element jdomElement) {
    this.jdomElement = jdomElement;
    this.modelBase = new JDomModelBase(jdomElement);

    super.setArtifactId(getChildElementTextTrim(POM_ELEMENT_ARTIFACT_ID, this.jdomElement));
    super.setDescription(getChildElementTextTrim(POM_ELEMENT_DESCRIPTION, this.jdomElement));
    super.setGroupId(getChildElementTextTrim(POM_ELEMENT_GROUP_ID, this.jdomElement));
    super.setModelVersion(getChildElementTextTrim(POM_ELEMENT_MODEL_VERSION, this.jdomElement));
    super.setName(getChildElementTextTrim(POM_ELEMENT_NAME, this.jdomElement));
    super.setPackaging(getChildElementTextTrim(POM_ELEMENT_PACKAGING, this.jdomElement));
    super.setVersion(getChildElementTextTrim(POM_ELEMENT_VERSION, this.jdomElement));

    super.setProfiles(new JDomProfiles(getChildElement(POM_ELEMENT_PROFILES, this.jdomElement), this));

    Element buildElement = getChildElement(POM_ELEMENT_BUILD, this.jdomElement);
    if (buildElement != null) {
      super.setBuild(new JDomBuild(buildElement));
    }

    Element parentElement = getChildElement(POM_ELEMENT_PARENT, this.jdomElement);
    if (parentElement != null) {
      super.setParent(new JDomParent(parentElement));
    }

    Element scmElement = getChildElement(POM_ELEMENT_SCM, this.jdomElement);
    if (scmElement != null) {
      super.setScm(new JDomScm(scmElement));
    }
  }

  @Override
  public void setArtifactId(String artifactId) {
    rewriteElement(POM_ELEMENT_ARTIFACT_ID, artifactId, jdomElement);
    super.setArtifactId(artifactId);
  }

  @Override
  public void setBuild(Build build) {
    //noinspection IfStatementWithTooManyBranches
    if (build == null) {
      rewriteElement(POM_ELEMENT_BUILD, null, jdomElement);
      super.setBuild(null);
    } else if (build instanceof JDomBuild) {
      rewriteElement(((JDomBuild) build).getJDomElement(), jdomElement);
      super.setBuild(build);
    } else {
      JDomBuild jdomBuild = new JDomBuild(insertNewElement(POM_ELEMENT_BUILD, jdomElement), build);
      super.setBuild(jdomBuild);
    }
  }

  @Override
  public CiManagement getCiManagement() {
    // TODO Implement support for setting Model#ciManagement (in #JDomModel and #setCiManagement) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCiManagement(CiManagement ciManagement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Contributor> getContributors() {
    // TODO Implement support for setting Model#contributors (in #JDomModel and #setContributors) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setContributors(List<Contributor> contributors) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Dependency> getDependencies() {
    // TODO Implement support for setting ModelBase#dependencies (in JDomModelBase#JDomModelBase and JDomModelBase#setDependencies) and remove this method override
    return modelBase.getDependencies();
  }

  @Override
  public void setDependencies(List<Dependency> dependencies) {
    modelBase.setDependencies(dependencies);
  }

  @Override
  public DependencyManagement getDependencyManagement() {
    // TODO Implement support for setting ModelBase#dependenyManagement (in JDomModelBase#JDomModelBase and JDomModelBase#setDependenyManagement) and remove this method override
    return modelBase.getDependencyManagement();
  }

  @Override
  public void setDependencyManagement(DependencyManagement dependencyManagement) {
    modelBase.setDependencyManagement(dependencyManagement);
  }

  @Override
  public void setDescription(String description) {
    rewriteElement(POM_ELEMENT_DESCRIPTION, description, jdomElement);
    super.setDescription(description);
  }

  @Override
  public List<Developer> getDevelopers() {
    // TODO Implement support for setting Model#developers (in #JDomModel and #setDevelopers) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDevelopers(List<Developer> developers) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DistributionManagement getDistributionManagement() {
    // TODO Implement support for setting ModelBase#distributionManagement (in JDomModelBase#JDomModelBase and JDomModelBase#setDistributionManagement) and remove this method override
    return modelBase.getDistributionManagement();
  }

  @Override
  public void setDistributionManagement(DistributionManagement distributionManagement) {
    modelBase.setDistributionManagement(distributionManagement);
  }

  @Override
  public void setGroupId(String groupId) {
    String projectGroupId = super.getGroupId();
    if (projectGroupId != null) {
      rewriteElement(POM_ELEMENT_GROUP_ID, groupId, jdomElement);
      super.setGroupId(groupId);
    } else {
      Parent parent = getParent();
      if (parent == null || !groupId.equals(parent.getGroupId())) {
        rewriteElement(POM_ELEMENT_GROUP_ID, groupId, jdomElement);
        super.setGroupId(groupId);
      }
    }
  }

  @Override
  public void setInceptionYear(String inceptionYear) {
    rewriteElement(POM_ELEMENT_INCEPTION_YEAR, inceptionYear, jdomElement);
    super.setInceptionYear(inceptionYear);
  }

  @Override
  public IssueManagement getIssueManagement() {
    // TODO Implement support for setting Model#issueManagement (in #JDomModel and #setIssueManagement) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setIssueManagement(IssueManagement issueManagement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<License> getLicenses() {
    // TODO Implement support for setting Model#licenses (in #JDomModel and #setLicenses) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLicenses(List<License> licenses) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<MailingList> getMailingLists() {
    // TODO Implement support for setting Model#mailingLists (in #JDomModel and #setMailingLists) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setMailingLists(List<MailingList> mailingLists) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setModelVersion(String modelVersion) {
    rewriteElement(POM_ELEMENT_MODEL_VERSION, modelVersion, jdomElement);
    super.setModelVersion(modelVersion);
  }

  @Override
  public List<String> getModules() {
    // TODO Implement support for setting ModelBase#modules (in JDomModelBase#JDomModelBase and JDomModelBase#setModules) and remove this method override
    return modelBase.getModules();
  }

  @Override
  public void setModules(List<String> modules) {
    modelBase.setModules(modules);
  }

  @Override
  public void setName(String name) {
    rewriteElement(POM_ELEMENT_NAME, name, jdomElement);
    super.setName(name);
  }

  @Override
  public Organization getOrganization() {
    // TODO Implement support for setting Model#organization (in #JDomModel and #setOrganization) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setOrganization(Organization organization) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setPackaging(String packaging) {
    rewriteElement(POM_ELEMENT_PACKAGING, packaging, jdomElement);
    super.setPackaging(packaging);
  }

  @Override
  @SuppressWarnings("OverlyComplexMethod")
  public void setParent(Parent parent) {
    Parent oldParent = getParent();

    //noinspection IfStatementWithTooManyBranches
    if (parent == null) {
      rewriteElement(POM_ELEMENT_PARENT, null, jdomElement);
      super.setParent(null);
    } else if (parent instanceof JDomParent) {
      rewriteElement(((JDomParent) parent).getJDomElement(), jdomElement);
      super.setParent(parent);
    } else {
      JDomParent jdomParent = new JDomParent(insertNewElement(POM_ELEMENT_PARENT, jdomElement), parent);
      super.setParent(jdomParent);
    }

    if (getGroupId() == null && (parent == null || !parent.getGroupId().equals(oldParent.getGroupId()))) {
      setGroupId(oldParent.getGroupId());
    }
    if (getVersion() == null && (parent == null || !parent.getVersion().equals(oldParent.getVersion()))) {
      setVersion(oldParent.getVersion());
    }
  }

  @Override
  public List<Repository> getPluginRepositories() {
    // TODO Implement support for setting ModelBase#pluginRepositories (in JDomModelBase#JDomModelBase and JDomModelBase#setPluginRepositories) and remove this method override
    return modelBase.getPluginRepositories();
  }

  @Override
  public void setPluginRepositories(List<Repository> pluginRepositories) {
    modelBase.setPluginRepositories(pluginRepositories);
  }

  @Override
  public Prerequisites getPrerequisites() {
    // TODO Implement support for setting Model#prerequisites (in #JDomModel and #setPrerequisites) and remove this method override
    throw new UnsupportedOperationException();
  }

  @Override
  public void setPrerequisites(Prerequisites prerequisites) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setProfiles(List<Profile> profiles) {
    //noinspection IfStatementWithTooManyBranches
    if (profiles == null) {
      rewriteElement(POM_ELEMENT_PROFILES, null, jdomElement);
      super.setProfiles(null);
    } else if (profiles instanceof JDomProfiles) {
      rewriteElement(((JDomProfiles) profiles).getJDomElement(), jdomElement);
      super.setProfiles(profiles);
    } else {
      @SuppressWarnings("TypeMayBeWeakened")
      JDomProfiles jdomProfiles = new JDomProfiles(insertNewElement(POM_ELEMENT_PROFILES, jdomElement), this, profiles);
      super.setProfiles(jdomProfiles);
    }
  }

  @Override
  public Properties getProperties() {
    // TODO Implement support for setting ModelBase#properties (in JDomModelBase#JDomModelBase and JDomModelBase#setProperties) and remove this method overrid
    return modelBase.getProperties();
  }

  @Override
  public void setProperties(Properties properties) {
    modelBase.setProperties(properties);
  }

  @Override
  public Reporting getReporting() {
    // TODO Implement support for setting ModelBase#reporting (in JDomModelBase#JDomModelBase and JDomModelBase#setReporting) and remove this method override
    return modelBase.getReporting();
  }

  @Override
  public void setReporting(Reporting reporting) {
    modelBase.setReporting(reporting);
  }

  @Override
  public List<Repository> getRepositories() {
    // TODO Implement support for setting ModelBase#repositories (in JDomModelBase#JDomModelBase and JDomModelBase#setRepositories) and remove this method override
    return modelBase.getRepositories();
  }

  @Override
  public void setRepositories(List<Repository> repositories) {
    modelBase.setRepositories(repositories);
  }

  @Override
  public void setScm(Scm scm) {
    //noinspection IfStatementWithTooManyBranches
    if (scm == null) {
      rewriteElement(POM_ELEMENT_SCM, null, jdomElement);
      super.setScm(null);
    } else if (scm instanceof JDomScm) {
      rewriteElement(((JDomScm) scm).getJDomElement(), jdomElement);
      super.setScm(scm);
    } else {
      JDomScm jdomScm = new JDomScm(insertNewElement(POM_ELEMENT_SCM, jdomElement), scm);
      super.setScm(jdomScm);
    }
  }

  @Override
  public void setUrl(String url) {
    rewriteElement(POM_ELEMENT_URL, url, jdomElement);
    super.setUrl(url);
  }

  @Override
  public void setVersion(String version) {
    String projectVersion = super.getVersion();
    if (projectVersion != null) {
      rewriteElement(POM_ELEMENT_VERSION, version, jdomElement);
      super.setVersion(version);
    } else {
      Parent parent = getParent();
      if (parent == null || !version.equals(parent.getVersion())) {
        rewriteElement(POM_ELEMENT_VERSION, version, jdomElement);
        super.setVersion(version);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public Model clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
