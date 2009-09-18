package org.ant4eclipse.jdt.internal.tools;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

import java.util.EmptyStackException;
import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathResolverContextImpl implements ClasspathResolverContext {

  private ClasspathEntryResolverExecutor _executor;

  private ResolverJob                    _resolverJob;

  private ResolvedClasspathImpl          _resolvedClasspath;

  public ClasspathResolverContextImpl(ClasspathEntryResolverExecutor executor, ResolverJob resolverJob,
      ResolvedClasspathImpl resolvedClasspath) {
    this._executor = executor;
    this._resolverJob = resolverJob;
    this._resolvedClasspath = resolvedClasspath;
  }

  public ClasspathResolverContextImpl(ClasspathEntryResolverExecutor executor, ResolverJob resolverJob) {
    this(executor, resolverJob, null);
  }

  public EclipseProject getCurrentProject() {
    try {
      return this._executor.getCurrentProject();
    } catch (EmptyStackException e) {
      return null;
    }
  }

  public boolean hasCurrentProject() {
    return this._executor.hasCurrentProject();
  }

  public void resolveProjectClasspath(EclipseProject project) {
    this._executor.resolveReferencedProject(project);
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    return this._resolverJob.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isCurrentProjectRoot() {
    return this._resolverJob.getRootProject().equals(getCurrentProject());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isRuntime() {
    return this._resolverJob.isRuntimeClasspath();
  }

  /**
   * {@inheritDoc}
   */
  public final List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._resolverJob.getJdtClasspathContainerArguments();
  }

  public JdtClasspathContainerArgument getJdtClasspathContainerArgument(String key) {
    Assert.nonEmpty(key);

    List<JdtClasspathContainerArgument> arguments = this._resolverJob.getJdtClasspathContainerArguments();

    // System.err.println(arguments);

    for (JdtClasspathContainerArgument jdtClasspathContainerArgument : arguments) {
      // System.err.println("'" + jdtClasspathContainerArgument.getKey() + "'");
      // System.err.println("'" + key + "'");
      if (key.equals(jdtClasspathContainerArgument.getKey())) {
        return jdtClasspathContainerArgument;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceRelative() {
    return this._resolverJob.isRelative();
  }

  public void setBootClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._resolvedClasspath != null) {
      this._resolvedClasspath.addBootClasspathEntry(resolvedClasspathEntry);
    }
  }

  public void addClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._resolvedClasspath != null) {
      this._resolvedClasspath.addClasspathEntry(resolvedClasspathEntry);
    }
  }

  public void addReferencedProjects(EclipseProject eclipseProject) {
    this._executor.addReferencedProject(eclipseProject);
  }
}
