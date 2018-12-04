package org.apache.maven.model.jdom.it;

import java.util.List;

import org.apache.maven.model.Plugin;

/**
 * Tests transformations of {@code plugins} in {@code pluginManagement}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PluginManagementEtlIT extends PluginsEtlIT
{
    @Override
    protected List<Plugin> getPluginsFromModel()
    {
        return jDomModelETL.getModel().getBuild().getPluginManagement().getPlugins();
    }
}
