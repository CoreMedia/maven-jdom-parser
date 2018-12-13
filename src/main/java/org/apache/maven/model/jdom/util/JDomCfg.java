package org.apache.maven.model.jdom.util;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDomCfg
{
    private static JDomCfg instance = new JDomCfg();

    private Map<String, List<String>> elementOrder = new HashMap<>();

    // Singleton
    private JDomCfg()
    {
        elementOrder.put( "project", asList(
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
            )
        );
        elementOrder.put( "profile", asList(
            "id",
            "activation",
            "modules",
            "distributionManagement",
            "properties",
            "dependencyManagement",
            "dependencies",
            "repositories",
            "pluginRepositories",
            "build",
            "reporting"
            )
        );

    }

    public static JDomCfg getInstance()
    {
        return instance;
    }

    public List<String> getElementOrder( String type )
    {
        return elementOrder.get( type );
    }

    public void setElementOrder( String type, List<String> elementOrder )
    {
        this.elementOrder.put( type, elementOrder );
    }
}
