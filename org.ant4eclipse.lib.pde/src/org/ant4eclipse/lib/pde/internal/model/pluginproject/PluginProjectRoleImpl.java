/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.pde.internal.model.pluginproject;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * Implements the eclipse plug-in project role. The plug-in project role contains the BundleDescription and the build
 * properties.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginProjectRoleImpl extends AbstractProjectRole implements PluginProjectRole {

  /** PLUGIN_NATURE */
  public static final String    PLUGIN_NATURE            = "org.eclipse.pde.PluginNature";

  /** PLUGIN_PROJECT_ROLE_NAME */
  public static final String    PLUGIN_PROJECT_ROLE_NAME = "PluginProjectRole";

  /** the BundleDescription of the underlying plug-in project */
  private BundleDescription     _bundleDescription;

  /** the build properties */
  private PluginBuildProperties _buildProperties;

  /**
   * <p>
   * Returns the plug-in project role. If a plug-in project role is not set, an exception will be thrown.
   * </p>
   * 
   * @return the plugin project role.
   */
  public static final PluginProjectRoleImpl getPluginProjectRole(EclipseProject eclipseProject) {
    Assure.assertTrue(hasPluginProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
        + "\" must have PluginProjectRole!");

    return eclipseProject.getRole(PluginProjectRoleImpl.class);
  }

  /**
   * <p>
   * Returns whether a plug-in project role is set or not.
   * </p>
   * 
   * @return whether a plug-in project role is set or not.
   */
  public static final boolean hasPluginProjectRole(EclipseProject eclipseProject) {
    Assure.notNull("eclipseProject", eclipseProject);
    return eclipseProject.hasRole(PluginProjectRoleImpl.class);
  }

  /**
   * <p>
   * Creates a new instance of type PluginProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the plugin project.
   */
  public PluginProjectRoleImpl(EclipseProject eclipseProject, BundleDescription description) {
    super(PLUGIN_PROJECT_ROLE_NAME, eclipseProject);
    Assure.notNull("eclipseProject", eclipseProject);
    Assure.notNull("description", description);
    this._bundleDescription = description;
    this._buildProperties = null;
  }

  /**
   * {@inheritDoc}
   */
  public BundleDescription getBundleDescription() {
    return this._bundleDescription;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasBuildProperties() {
    return this._buildProperties != null;
  }

  /**
   * {@inheritDoc}
   */
  public PluginBuildProperties getBuildProperties() {
    return this._buildProperties;
  }

  /**
   * {@inheritDoc}
   */
  public void setBuildProperties(PluginBuildProperties buildProperties) {
    this._buildProperties = buildProperties;
  }

}
