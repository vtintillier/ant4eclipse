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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.ClassName;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.junit.Assert;

import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Adds JDT-specific features to {@link EclipseProjectBuilder}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class JdtProjectBuilder extends EclipseProjectBuilder {

  private List<String>                   _classpathEntries;

  /**
   * Holds all SourceClasses (grouped by their source folders) that should be added to this project.
   * 
   * <ul>
   * <li>Key: the source folder
   * <li>Value: {@link SourceClasses} the SourceClasses in the source folder
   * </ul>
   */
  private Map<String, List<SourceClass>> _sourceClasses;

  /**
   * Returns a "pre-configured" {@link JdtProjectBuilder}, that already has set:
   * <ul>
   * <li>a java builder</li>
   * <li>the java nature
   * <li>
   * <li>the JRE container classpath entry</li>
   * <li>a source folder (<tt>src</tt>)</li>
   * <li>a default output folder (<tt>bin</tt>)</li>
   * </ul>
   * 
   * The builder returned can be used to further customize the project
   * 
   * @param projectName
   *          The name of the project
   * @return the pre-configured JdtEclipseProjectBuilder
   */
  public static JdtProjectBuilder getPreConfiguredJdtBuilder(String projectName) {
    JdtProjectBuilder result = new JdtProjectBuilder(projectName);
    result.withJreContainerClasspathEntry();
    result.withSrcClasspathEntry("src", false);
    result.withOutputClasspathEntry("bin");
    return result;
  }

  /**
   * Constructs a new JdtEclipseProjectBuilder instance with the project name.
   * 
   * <p>
   * The constructor adds a {@link #withJavaBuilder() javaBuilder} and a {@link #withJavaNature() javaNature} to the
   * project
   * 
   * @param projectName
   */
  public JdtProjectBuilder(String projectName) {
    super(projectName);

    this._sourceClasses = new Hashtable<String, List<SourceClass>>();
    this._classpathEntries = new LinkedList<String>();

    // All JDT-Projects have a java builder and a java nature
    withJavaNature();
    withJavaBuilder();
  }

  /**
   * Adds a {@link SourceClass} to this project
   * 
   * @param sourceFolder
   *          the sourcefolder for this class
   * @param className
   *          The full qualified classname for this class
   * @return the {@link SourceClass} instance
   */
  public SourceClass withSourceClass(String sourceFolder, String className) {
    SourceClass sourceClass = new SourceClass(this, className);
    List<SourceClass> sourceClasses = this._sourceClasses.get(sourceFolder);
    if (sourceClasses == null) {
      sourceClasses = new LinkedList<SourceClass>();
      this._sourceClasses.put(sourceFolder, sourceClasses);
    }
    sourceClasses.add(sourceClass);
    return sourceClass;
  }

  protected JdtProjectBuilder withJavaBuilder() {
    return (JdtProjectBuilder) withBuilder("org.eclipse.jdt.core.javabuilder");
  }

  /**
   * Adds <tt>entry</tt> to the list of classpath entries for this project.
   * 
   * <p>
   * the classpath entry must be a well formatted line of xml, that is valid in a <tt>.classpath</tt> file.
   * 
   * @param entry
   *          The xml-entry
   * @return this
   */
  public JdtProjectBuilder withClasspathEntry(String entry) {
    Assert.assertNotNull(entry);
    this._classpathEntries.add(entry);
    return this;
  }

  protected JdtProjectBuilder withJavaNature() {
    return (JdtProjectBuilder) withNature(JavaProjectRole.JAVA_NATURE);
  }

  public JdtProjectBuilder withSrcClasspathEntry(String path, boolean exported) {
    String line = String
        .format("<classpathentry kind='src' path='%s' exported='%s'/>", path, Boolean.valueOf(exported));
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withSrcClasspathEntry(String path, String output, boolean exported) {
    String line = String.format("<classpathentry kind='src' path='%s' output='%s' exported='%s'/>", path, output,
        Boolean.valueOf(exported));
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withContainerClasspathEntry(String path) {
    String line = String.format("<classpathentry kind='con' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withVarClasspathEntry(String path) {
    String line = String.format("<classpathentry kind='var' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withOutputClasspathEntry(String path) {
    String line = String.format(" <classpathentry kind='output' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  /**
   * Adds a JRE_CONTAINER to the classpath
   * 
   * @return
   */
  public JdtProjectBuilder withJreContainerClasspathEntry() {
    return withClasspathEntry("<classpathentry kind='con' path='org.eclipse.jdt.launching.JRE_CONTAINER'/>");
  }

  /**
   * Adds a JRE_CONTAINER with the given containerName to the classpath
   * 
   * @return
   */
  public JdtProjectBuilder withJreContainerClasspathEntry(String containerName) {
    return withClasspathEntry(String.format(
        "<classpathentry kind='con' path='org.eclipse.jdt.launching.JRE_CONTAINER/%s'/>", containerName));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts(File projectDir) {
    super.createArtefacts(projectDir);

    createClasspathFile(projectDir);

    for (Map.Entry<String, List<SourceClass>> entry : this._sourceClasses.entrySet()) {
      String sourceFolder = entry.getKey();
      List<SourceClass> sourceClasses = entry.getValue();
      for (SourceClass sourceClass : sourceClasses) {
        createSourceClass(projectDir, sourceFolder, sourceClass);
      }
    }
  }

  protected void createSourceClass(File projectDir, String sourceFolder, SourceClass sourceClass) {
    File sourceDir = new File(projectDir, sourceFolder);
    if (!sourceDir.exists()) {
      sourceDir.mkdirs();
    }
    Assert.assertTrue(sourceDir.isDirectory());

    ClassName className = ClassName.fromQualifiedClassName(sourceClass.getClassName());
    File packageDir = new File(sourceDir, className.getPackageAsDirectoryName());
    if (!packageDir.exists()) {
      packageDir.mkdirs();
    }
    Assert.assertTrue(packageDir.isDirectory());

    File sourcefile = new File(sourceDir, className.asSourceFileName());

    StringTemplate classTemplate = new StringTemplate();
    classTemplate.append("package ${packageName};").nl();
    classTemplate.append("public class ${className} {").nl();
    classTemplate.append(sourceClass.generateUsageCode()).nl();
    classTemplate.append("} // end of class").nl();

    classTemplate.replace("packageName", className.getPackageName());
    classTemplate.replace("className", className.getClassName());

    Utilities.writeFile(sourcefile, classTemplate.toString(), Utilities.ENCODING);
  }

  protected void createClasspathFile(File projectDir) {
    StringBuilder dotClasspath = new StringBuilder();
    dotClasspath.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(Utilities.NL).append("<classpath>")
        .append(Utilities.NL);

    for (String entry : this._classpathEntries) {
      dotClasspath.append(entry).append(Utilities.NL);
    }
    dotClasspath.append("</classpath>").append(Utilities.NL);

    File dotClasspathFile = new File(projectDir, ".classpath");
    Utilities.writeFile(dotClasspathFile, dotClasspath.toString(), "UTF-8");
  }

}
