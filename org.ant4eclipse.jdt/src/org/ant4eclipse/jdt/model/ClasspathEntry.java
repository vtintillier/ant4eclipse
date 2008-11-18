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
package org.ant4eclipse.jdt.model;

/**
 * {@link ClasspathEntry}
 */
public interface ClasspathEntry {

  /**
   * Returns the entryKind.
   * 
   * @return the entryKind.
   */
  public int getEntryKind();

  /**
   * Returns the path.
   * 
   * @return the path.
   */
  public String getPath();
}
