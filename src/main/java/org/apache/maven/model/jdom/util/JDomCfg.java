package org.apache.maven.model.jdom.util;

import static java.util.Arrays.asList;

import java.util.List;

public class JDomCfg
{
    private static JDomCfg instance = new JDomCfg();

    private List<String> elementOrder = asList(
        "modelVersion",
        "",
        "parent",
        "",
        "groupId",
        "artifactId",
        "version",
        "packaging",
        "",
        "name",
        "description",
        "url",
        "inceptionYear",
        "organization",
        "licenses",
        "",
        "developers",
        "contributors",
        "",
        "mailingLists",
        "",
        "prerequisites",
        "",
        "modules",
        "",
        "scm",
        "issueManagement",
        "ciManagement",
        "distributionManagement",
        "",
        "properties",
        "",
        "dependencyManagement",
        "dependencies",
        "",
        "repositories",
        "pluginRepositories",
        "",
        "build",
        "",
        "reporting",
        "",
        "profiles"
    );

    private JDomCfg()
    {
        // Singleton
    }

    public static JDomCfg getInstance()
    {
        return instance;
    }

    public List<String> getElementOrder()
    {
        return elementOrder;
    }

    public void setElementOrder( List<String> elementOrder )
    {
        this.elementOrder = elementOrder;
    }
}
