package org.ant4eclipse.platform.internal.model.launcher;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import org.ant4eclipse.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.platform.model.launcher.LaunchConfigurationReader;
import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.ant4eclipse.testframework.TestDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LaunchConfigurationReaderImplTest extends ConfigurableAnt4EclipseTestCase {

  private TestDirectory _testWorkspace;

  @Override
  @Before
  public void setup() {
    super.setup();

    this._testWorkspace = new TestDirectory();
  }

  @After
  @Override
  public void dispose() {
    this._testWorkspace.dispose();

    super.dispose();
  }

  @Test
  public void test_JdtLaunchConfig() throws Exception {
    LaunchConfigurationReader launchConfigurationReader = LaunchConfigurationReader.Helper.getReader();
    InputStream inputStream = getResource("LocalJavaApplication.txt");
    File launchConfigurationFile = this._testWorkspace.createFile("LocalJavaApplication.launch", inputStream);
    LaunchConfiguration launchConfiguration = launchConfigurationReader
        .readLaunchConfiguration(launchConfigurationFile);

    assertNotNull(launchConfiguration);
    assertEquals("org.eclipse.jdt.launching.localJavaApplication", launchConfiguration.getType());

    assertTrue(launchConfiguration.getBooleanAttribute("myBoolean"));
    assertEquals("true", launchConfiguration.getAttribute("myBoolean"));
    assertFalse(launchConfiguration.getBooleanAttribute("org.eclipse.jdt.launching.DEFAULT_CLASSPATH"));
    assertEquals("/c/src/main/Main.java,/c/src/main/MainHelper.java", launchConfiguration
        .getAttribute("org.eclipse.debug.core.MAPPED_RESOURCE_PATHS"));
    String[] elements = launchConfiguration.getListAttribute("org.eclipse.debug.core.MAPPED_RESOURCE_PATHS");
    assertNotNull(elements);
    assertEquals(2, elements.length);
    assertEquals("/c/src/main/Main.java", elements[0]);
    assertEquals("/c/src/main/MainHelper.java", elements[1]);
  }
}