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
 * A raw path entry is a reference to another entity which has not been resolved. It's only used to represent the data
 * stored within the configuration file.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class RawPathEntry {

  private ReferenceKind _kind;

  private String        _value;

  private boolean       _exported;

  private boolean       _external;

  private String        _projectname;

  /**
   * Creates a new description used to access a referred entity within a project.
   * 
   * @param projectname
   *          The name of the owning project. Neither <code>null</code> nor empty.
   * @param refkind
   *          The kind of reference to the entity. Not <code>null</code>.
   * @param value
   *          The value used to describe the entity (in most cases a simple path).
   * @param export
   *          <code>true</code> <=> Contribute this record to dependant resources as well.
   * @param external
   *          <code>true</code> <=> The value doesn't refer to a location within the workspace.
   */
  public RawPathEntry( String projectname, ReferenceKind refkind, String value, boolean export, boolean external ) {
    Assure.nonEmpty( "projectname", projectname );
    Assure.notNull( "refkind", refkind );
    _projectname = projectname;
    _kind = refkind;
    _value = value;
    _exported = export;
    _external = external;
  }

  /**
   * Returns the name of the owning project.
   * 
   * @return The name of the owning project. Neither <code>null</code> nor empty.
   */
  public String getProjectname() {
    return _projectname;
  }

  /**
   * Returns the kind of reference established by this entry.
   * 
   * @return The kind of reference established by this entry. Not <code>null</code>.
   */
  public ReferenceKind getKind() {
    return _kind;
  }

  /**
   * Returns the value used to refer to a specified entity. In nearly all cases this might be a path.
   * 
   * @return The value used to refer to a specified entity. Neither <code>null</code> nor empty.
   */
  public String getValue() {
    return _value;
  }

  /**
   * Returns <code>true</code> if this entry is contributed to dependant resources, too.
   * 
   * @return <code>true</code> <=> Contribute this entry to dependant resources, too.
   */
  public boolean isExported() {
    return _exported;
  }

  /**
   * Returns <code>true</code> if this entry doesn't refer to a location within the workspace.
   * 
   * @return <code>true</code> <=> This entry doesn't refer to a location within the workspace.
   */
  public boolean isExternal() {
    return _external;
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
    RawPathEntry other = (RawPathEntry) object;
    if( !_projectname.equals( other._projectname ) ) {
      return false;
    }
    if( _kind != other._kind ) {
      return false;
    }
    if( _external != other._external ) {
      return false;
    }
    if( _value == null ) {
      return other._value == null;
    } else {
      return _value.equals( other._value );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    /**
     * @note [31-Jul-2009:KASI] The flag <code>_exported</code> is not part of the hash code as it's not a configuration
     *       of the path entry. It's a flag used for the project which tells how to deal with this entry.
     */
    int result = 1;
    result = 31 * result + _projectname.hashCode();
    result = 31 * result + _kind.hashCode();
    result = 31 * result + (_value != null ? _value.hashCode() : 0);
    result = 31 * result + (_external ? 1 : 0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[RawPathEntry: _projectname: %s, _kind: %s, _value: %s, _exported: %s, _external: %s]",
        _projectname, _kind, _value, _exported, _external );
  }

} /* ENDCLASS */
