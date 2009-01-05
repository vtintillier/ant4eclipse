package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractTestWorkspaceBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

public class HasNatureTest extends AbstractTestWorkspaceBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("hasNature.xml");

    new EclipseProjectBuilder("simpleproject").withNature("org.ant4eclipse.testnature").createIn(
        getTestWorkspaceDirectory());
  }

  public void testNonexistingNature() {
    expectLog("testNonexistingNature", "OK");
  }

  public void testExistingNature() {
    expectLog("testExistingNature", "OK");
  }
}
