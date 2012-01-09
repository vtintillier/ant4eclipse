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
package org.ant4eclipse.lib.pydt.model;

import org.ant4eclipse.lib.core.Assure;

/**
 * Resolved record used to identify a source folder within a project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedSourceEntry implements ResolvedPathEntry {

  private String _folder;

  private String _owningproject;

  /**
   * Sets up this entry with the relative path of the folder. The path is relative to the project.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param foldername
   *          The name of the folder. <code>null</code> or not empty.
   */
  public ResolvedSourceEntry( String owningproject, String foldername ) {
    Assure.nonEmpty( "owningproject", owningproject );
    _owningproject = owningproject;
    _folder = foldername;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOwningProjectname() {
    return _owningproject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReferenceKind getKind() {
    return ReferenceKind.Source;
  }

  /**
   * Returns the relative path of the folder within the project.
   * 
   * @return The relative path of the folder. <code>null</code> or not empty.
   */
  public String getFolder() {
    return _folder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object object ) {
    if( this == object ) {
      return true;
    }
    if( object == null ) {
      return false;
    }
    if( object.getClass() != getClass() ) {
      return false;
    }
    ResolvedSourceEntry other = (ResolvedSourceEntry) object;
    if( !_owningproject.equals( other._owningproject ) ) {
      return false;
    }
    if( _folder == null ) {
      return other._folder == null;
    }
    return _folder.equals( other._folder );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = _owningproject.hashCode();
    result = 31 * result + (_folder != null ? _folder.hashCode() : 0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[ResolvedSourceEntry: _owningproject: %s, _folder: %s]", _owningproject, _folder );
  }

} /* ENDCLASS */
