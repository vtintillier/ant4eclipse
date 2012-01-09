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
package org.ant4eclipse.ant.jdt.type;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Ant type to define class path containers. A class path container can be added to a project's class path.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClassPathContainerType extends AbstractAnt4EclipseDataType {

  private Union  _resources = null;
  private String _name;

  /**
   * <p>
   * Creates a new instance of type {@link JdtClassPathContainerType}.
   * </p>
   * 
   * @param project
   */
  public JdtClassPathContainerType( Project project ) {
    super( project );

    _resources = new Union();
  }

  /**
   * <p>
   * </p>
   * 
   * @return the name
   */
  public String getName() {
    return _name;
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   *          the name to set
   */
  public void setName( String name ) {
    _name = name;
  }

  /**
   * Add a collection of resources upon which to operate.
   * 
   * @param rc
   *          resource collection to add.
   */
  public void add( ResourceCollection rc ) {
    _resources.add( rc );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings( "unchecked" ) 
  protected void doValidate() {
    // TODO: validate

    // fetch the ClassPathElementsRegistry
    ClassPathElementsRegistry variablesRegistry = A4ECore.instance().getRequiredService(
        ClassPathElementsRegistry.class );

    // fetch the provided files
    Iterator<FileResource> iterator = _resources.iterator();
    List<File> files = new ArrayList<File>();
    while( iterator.hasNext() ) {
      files.add( iterator.next().getFile() );
    }

    // TODO: what to do if classpathContainer already registered?
    variablesRegistry.registerClassPathContainer( _name, files );
    
  }

} /* ENDCLASS */
