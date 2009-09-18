package org.ant4eclipse.platform.tools;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.util.List;

/**
 * <p>
 * {@link ReferencedProjectsResolver ReferencedProjectsResolvers} can be used to resolve projects that are (directly)
 * referenced by another project. It depends on the type of a given project how those references are defined. E.g. in
 * java projects references are usually defined through entries in the <code>.classpath</code> file.
 * </p>
 * <p>
 * For each dependency type a {@link ReferencedProjectsResolver} can be implemented. To contribute a
 * {@link ReferencedProjectsResolver} implementation to ant4eclipse, the resolver also has to be declared in the
 * <code>ant4eclipse-configuration.properties</code> file (e.g.
 * <code>referencedProjectsResolver.platform=org.ant4eclipse.platform.internal.tools.PlatformReferencedProjectsResolver</code>
 * .
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ReferencedProjectsResolver {

  /**
   * <p>
   * Returns if this {@link ReferencedProjectsResolver} can resolve referenced projects for the given project.
   * </p>
   * 
   * @param project
   *          the project
   * @return <code>true</code> if the {@link ReferencedProjectsResolver} can resolve referenced projects for the given
   *         project, otherwise <code>false</code>.
   */
  boolean canHandle(EclipseProject project);

  /**
   * <p>
   * Returns a list with all {@link EclipseProject EclipseProjects} that are directly referenced from the given
   * {@link EclipseProject}.
   * </p>
   * 
   * @param project
   *          the project
   * @param additionalElements
   *          in some cases it is necessary to provide additional information to resolve referenced projects. Maybe
   *          <code>null</code>.
   * 
   * @todo [07-Jul-2009:KASI] The parameter 'additionalElements' makes no sense since it has not been specified. An API
   *       as provided through this interface needs to make clear what has to be done with the provided data.
   * 
   * @return
   */
  List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements);
}
