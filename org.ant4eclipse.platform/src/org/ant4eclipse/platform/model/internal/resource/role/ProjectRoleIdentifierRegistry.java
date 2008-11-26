/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.model.internal.resource.role;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.model.internal.resource.EclipseProjectImpl;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

/**
 * The ProjectRoleIdentifierRegistry holds all known {@link ProjectRoleIdentifier}s. It can be used to apply roles to
 * {@link EclipseProjectImpl}s
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ProjectRoleIdentifierRegistry {

  /**
   * The prefix of properties that holds a RoleIdentifier class name
   */
  public final static String              ROLEIDENTIFIER_PREFIX = "roleidentifier";

  /**
   * All known {@link ProjectRoleIdentifier}
   */
  private Iterable<ProjectRoleIdentifier> _projectRoleIdentifiers;

  public ProjectRoleIdentifierRegistry() {
    init();
  }

  /**
   * Modifies the supplied project according to all currently registered RoleIdentifier instances.
   * 
   * @param project
   *          The project that shall be modified.
   */
  public void applyRoles(final EclipseProjectImpl project) {
    for (ProjectRoleIdentifier projectRoleIdentifier : _projectRoleIdentifiers) {
      if (projectRoleIdentifier.isRoleSupported(project)) {
        final ProjectRole projectRole = projectRoleIdentifier.createRole(project);
        project.addRole(projectRole);
      }
    }
  }

  public Iterable<ProjectRoleIdentifier> getProjectRoleIdentifiers() {
    return _projectRoleIdentifiers;
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {
    // get all properties that defines a ProjectRoleIdentifier
    Iterable<String[]> roleidentifierEntries = Ant4EclipseConfiguration.Helper.getAnt4EclipseConfiguration()
        .getAllProperties(ROLEIDENTIFIER_PREFIX);

    final List<ProjectRoleIdentifier> roleIdentifiers = new LinkedList<ProjectRoleIdentifier>();

    // Instantiate all ProjectRoleIdentifiers
    for (String[] roleidentifierEntry : roleidentifierEntries) {
      // we're not interested in the key of a roleidentifier. only the classname (value of the entry) is relevant
      String roleidentiferClassName = roleidentifierEntry[1];
      ProjectRoleIdentifier roleIdentifier = Utilities.newInstance(roleidentiferClassName);
      A4ELogging.trace("Register ProjectRoleIdentifier '%s'", new Object[] { roleIdentifier });
      roleIdentifiers.add(roleIdentifier);
    }

    this._projectRoleIdentifiers = roleIdentifiers;
  }

}
