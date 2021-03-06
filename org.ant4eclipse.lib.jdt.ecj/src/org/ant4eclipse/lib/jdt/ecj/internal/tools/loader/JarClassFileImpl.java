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
package org.ant4eclipse.lib.jdt.ecj.internal.tools.loader;

import java.io.IOException;
import java.util.zip.ZipFile;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.jdt.ecj.ClassFile;
import org.ant4eclipse.lib.jdt.ecj.EcjExceptionCodes;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.DefaultReferableType;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * DefaultClassFile --
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class JarClassFileImpl extends DefaultReferableType implements ClassFile {

  /** the zip file */
  private ZipFile _zipFile;

  /** the zip entry name */
  private String  _zipEntryName;

  /**
   * @param zipEntryName
   * @param zipFile
   */
  public JarClassFileImpl(String zipEntryName, ZipFile zipFile, String libraryLocation, byte libraryType) {

    super(libraryLocation, libraryType);

    Assure.nonEmpty("zipEntryName", zipEntryName);
    Assure.notNull("zipFile", zipFile);

    this._zipEntryName = zipEntryName;
    this._zipFile = zipFile;
  }

  public byte[] getBytes() {
    try {
      return Util.getZipEntryByteContent(this._zipFile.getEntry(this._zipEntryName), this._zipFile);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final IBinaryType getBinaryType() {
    try {
      return ClassFileReader.read(this._zipFile, this._zipEntryName, true);
    } catch (ClassFormatException e) {
      throw new Ant4EclipseException(e, EcjExceptionCodes.UNABLE_TO_READ_BINARY_TYPE_FROM_JAR_EXCEPTION, this._zipFile,
          this._zipEntryName);
    } catch (IOException e) {
      throw new Ant4EclipseException(e, EcjExceptionCodes.UNABLE_TO_READ_BINARY_TYPE_FROM_JAR_EXCEPTION, this._zipFile,
          this._zipEntryName);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[JarClassFileImpl:");
    buffer.append(" bundleLocation: ");
    buffer.append(getLibraryLocation());
    buffer.append(" bundleType: ");
    buffer.append(getLibraryType());
    buffer.append(" accessRestriction: ");
    buffer.append(getAccessRestriction());
    buffer.append(" zipFile: ");
    buffer.append(this._zipFile);
    buffer.append(" zipEntryName: ");
    buffer.append(this._zipEntryName);
    buffer.append("]");
    return buffer.toString();
  }

}
