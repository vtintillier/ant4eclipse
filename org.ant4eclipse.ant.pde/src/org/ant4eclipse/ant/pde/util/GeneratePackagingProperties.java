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
package org.ant4eclipse.ant.pde.util;

import org.ant4eclipse.lib.core.util.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GeneratePackagingProperties {

  private String                   ws;
  private String                   os;
  private String                   arch;
  
  private Map<String,List<String>> fileMap;
  private List<String>             fileList;

  /**
   * <p>
   * </p>
   * 
   * @param args
   */
  public static void main( String[] args ) {

    File binDirectory = new File(
        "R:/software/ide/eclipse-3.5.2-delta-pack/eclipse/features/org.eclipse.equinox.executable_3.3.201.R35x_v20091211-7M-FngFELSU3Pqlv3JdZn/bin" );

    new GeneratePackagingProperties( binDirectory );
  }

  /**
   * <p>
   * Creates a new instance of type {@link GeneratePackagingProperties}.
   * </p>
   * 
   * @param binDirectory
   *          as
   */
  public GeneratePackagingProperties( File binDirectory ) {
    fileMap = new HashMap<String,List<String>>();
    dumpChildren( binDirectory, binDirectory, 0 );
    StringBuffer buffer = new StringBuffer();
    for( Entry<String,List<String>> entries : fileMap.entrySet() ) {
      buffer.setLength(0);
      for( Iterator iterator = entries.getValue().iterator(); iterator.hasNext(); ) {
        String value = (String) iterator.next();
        buffer.append( value );
        if( iterator.hasNext() ) {
          buffer.append( "," );
        }
      }
      System.out.println( String.format( "%s=%s", entries.getKey(), buffer.toString() ) );
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param directory
   * @param parentDirectory
   * @param level
   */
  private void dumpChildren( File directory, File parentDirectory, int level ) {

    for( File child : directory.listFiles() ) {

      switch( level ) {
      case 1:
        ws = directory.getName();
        break;
      case 2:
        os = directory.getName();
        break;
      case 3:
        arch = directory.getName();
        String key = String.format( "%s.%s.%s", ws, os, arch );
        if( !fileMap.containsKey( key ) ) {
          fileList = new ArrayList<String>();
          fileMap.put( key, fileList );
        }
        break;
      default:
        break;
      }

      if( child.isFile() ) {
        fileList.add( Utilities.calcRelative( parentDirectory, child ).replace( '\\', '/' ) );
      } else if( child.isDirectory() ) {
        if( level >= 3 ) {
          dumpChildren( child, parentDirectory, level + 1 );
        } else {
          dumpChildren( child, child, level + 1 );
        }
      }
    }
  }
  
} /* ENDCLASS */
