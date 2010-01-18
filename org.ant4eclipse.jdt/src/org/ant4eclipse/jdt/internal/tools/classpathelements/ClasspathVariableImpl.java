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
package org.ant4eclipse.jdt.internal.tools.classpathelements;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.tools.classpathelements.ClassPathVariable;

import java.io.File;

/**
 * <p>
 * Implementation of {@link ClassPathVariable}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathVariableImpl implements ClassPathVariable {

  /** the name of the class path variable */
  private String _name;

  /** the path of this class path variable */
  private File   _path;

  /**
   * <p>
   * Creates a new instance of type ClasspathVariableImpl.
   * </p>
   * 
   * @param name
   * @param path
   */
  public ClasspathVariableImpl(String name, File path) {
    Assert.nonEmpty(name);
    Assert.notNull(path);

    this._name = name;
    this._path = path;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  public File getPath() {
    return this._path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    ClasspathVariableImpl castedObj = (ClasspathVariableImpl) o;
    return ((this._name == null ? castedObj._name == null : this._name.equals(castedObj._name)) && (this._path == null ? castedObj._path == null
        : this._path.equals(castedObj._path)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._name == null ? 0 : this._name.hashCode());
    hashCode = 31 * hashCode + (this._path == null ? 0 : this._path.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ClasspathVariableImpl:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(" _path: ");
    buffer.append(this._path);
    buffer.append("]");
    return buffer.toString();
  }

}
