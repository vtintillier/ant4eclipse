package org.ant4eclipse.pde.test.builder;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.testframework.AbstractTestDirectoryBasedTest;

public class PdeProjectBuilderTest extends AbstractTestDirectoryBasedTest {

	public void testPdeProjectFileSet_simple() {

		// create simple project
		PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder
				.getPreConfiguredPdeProjectBuilder("simpleproject1");
		pdeProjectBuilder.withSourceClass("@dot",
				"de.gerd-wuetherich.test.Gerd").finishClass();
		pdeProjectBuilder.withSourceClass("my/src",
				"de.gerd-wuetherich.test.Test").finishClass();
		pdeProjectBuilder.withSourceClass("my/src",
				"de.gerd-wuetherich.test.Test2").finishClass();
		pdeProjectBuilder.withSourceClass("my/src",
				"de.gerd-wuetherich.test.Test3").finishClass();
		pdeProjectBuilder.createIn(getTestDirectoryRootDir());

		File root = getTestDirectoryRootDir();

		// assert
		Assert.isDirectory(root);

		File simpleProjectDir = new File(root, "simpleproject1");

		Assert.isDirectory(simpleProjectDir);

		assertChildren(simpleProjectDir, ".classpath", ".project", "@dot",
				"META-INF", "my");

		assertChildren(new File(simpleProjectDir, "@dot"), "de");
		assertChildren(new File(simpleProjectDir, "my"), "src");
		assertChildren(new File(simpleProjectDir, "META-INF"), "MANIFEST.MF");
	}

	/**
	 * @param parent
	 * @param names
	 */
	protected void assertChildren(File parent, String... names) {
		Assert.isDirectory(parent);

		File[] children = parent.listFiles();
		assertEquals(names.length, children.length);

		List<String> namesList = new LinkedList<String>(Arrays.asList(names));
		for (File file : children) {
			assertTrue(namesList.remove(file.getName()));
		}
	}
}