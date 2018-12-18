package org.apache.maven.model.jdom.it;

import org.apache.maven.model.PluginContainer;

/**
 * Tests transformations of {@code plugins} in {@code pluginManagement}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PluginManagementEtlIT extends PluginsEtlIT
{
    @Override
    protected PluginContainer getPluginPluginContainer()
    {
        return jDomModelETL.getModel().getBuild().getPluginManagement();
    }
}
