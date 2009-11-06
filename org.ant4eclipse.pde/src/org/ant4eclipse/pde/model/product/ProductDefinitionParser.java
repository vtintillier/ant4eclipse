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
package org.ant4eclipse.pde.model.product;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;

import org.osgi.framework.Version;

import java.io.InputStream;

/**
 * <p>
 * Parser for an eclipse product definition (<code>*.product</code>).
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProductDefinitionParser {

  /**
   * <p>
   * Creates a product definition from the supplied content.
   * </p>
   * 
   * @param inputstream
   *          The stream which provides the content. Not <code>null</code>.
   * 
   * @return A product definition instance. Not <code>null</code>.
   */
  public static ProductDefinition parseProductDefinition(InputStream inputstream) {

    Assert.notNull(inputstream);

    XQueryHandler queryhandler = new XQueryHandler();

    XQuery namequery = queryhandler.createQuery("//product/@name");
    XQuery idquery = queryhandler.createQuery("//product/@id");
    XQuery applicationquery = queryhandler.createQuery("//product/@application");
    XQuery versionquery = queryhandler.createQuery("//product/@version");
    XQuery usefeaturesquery = queryhandler.createQuery("//product/@useFeatures");

    XQuery launchernamequery = queryhandler.createQuery("//product/launcher/@name");
    XQuery splashquery = queryhandler.createQuery("//product/splash/@location");

    XQuery pluginidquery = queryhandler.createQuery("//product/plugins/plugin/@id");
    XQuery fragmentquery = queryhandler.createQuery("//product/plugins/plugin/@fragment");

    XQuery featureidquery = queryhandler.createQuery("//product/features/feature/@id");
    XQuery featureversionquery = queryhandler.createQuery("//product/features/feature/@version");

    XQuery configinilinuxquery = queryhandler.createQuery("//product/configIni/linux");
    XQuery configinimacosxquery = queryhandler.createQuery("//product/configIni/macosx");
    XQuery configinisolarisquery = queryhandler.createQuery("//product/configIni/solaris");
    XQuery configiniwin32query = queryhandler.createQuery("//product/configIni/win32");

    XQuery programallquery = queryhandler.createQuery("//product/launcherArgs/programArgs");
    XQuery programlinuxquery = queryhandler.createQuery("//product/launcherArgs/programArgsLin");
    XQuery programmacquery = queryhandler.createQuery("//product/launcherArgs/programArgsMac");
    XQuery programsolarisquery = queryhandler.createQuery("//product/launcherArgs/programArgsSol");
    XQuery programwinquery = queryhandler.createQuery("//product/launcherArgs/programArgsWin");

    XQuery vmargsallquery = queryhandler.createQuery("//product/launcherArgs/vmArgs");
    XQuery vmargslinuxquery = queryhandler.createQuery("//product/launcherArgs/vmArgsLin");
    XQuery vmargsmacquery = queryhandler.createQuery("//product/launcherArgs/vmArgsMac");
    XQuery vmargssolarisquery = queryhandler.createQuery("//product/launcherArgs/vmArgsSol");
    XQuery vmargswinquery = queryhandler.createQuery("//product/launcherArgs/vmArgsWin");

    XQuery vmlinuxquery = queryhandler.createQuery("//product/vm/linux");
    XQuery vmmacosxquery = queryhandler.createQuery("//product/vm/macosx");
    XQuery vmsolarisquery = queryhandler.createQuery("//product/vm/solaris");
    XQuery vmwin32query = queryhandler.createQuery("//product/vm/win32");

    XQueryHandler.queryInputStream(inputstream, queryhandler);

    ProductDefinition result = new ProductDefinition();

    result.setApplication(applicationquery.getSingleResult());
    result.setId(idquery.getSingleResult());
    result.setName(namequery.getSingleResult());
    result.setVersion(new Version(versionquery.getSingleResult()));
    result.setLaunchername(launchernamequery.getSingleResult());
    result.setSplashplugin(splashquery.getSingleResult());
    result.setBasedOnFeatures(Boolean.parseBoolean(usefeaturesquery.getSingleResult()));

    result.addConfigIni(ProductDefinition.Os.linux, configinilinuxquery.getSingleResult());
    result.addConfigIni(ProductDefinition.Os.macosx, configinimacosxquery.getSingleResult());
    result.addConfigIni(ProductDefinition.Os.solaris, configinisolarisquery.getSingleResult());
    result.addConfigIni(ProductDefinition.Os.win32, configiniwin32query.getSingleResult());

    result.addProgramArgs(ProductDefinition.Os.all, programallquery.getSingleResult());
    result.addProgramArgs(ProductDefinition.Os.linux, programlinuxquery.getSingleResult());
    result.addProgramArgs(ProductDefinition.Os.macosx, programmacquery.getSingleResult());
    result.addProgramArgs(ProductDefinition.Os.solaris, programsolarisquery.getSingleResult());
    result.addProgramArgs(ProductDefinition.Os.win32, programwinquery.getSingleResult());

    result.addVmArgs(ProductDefinition.Os.all, vmargsallquery.getSingleResult());
    result.addVmArgs(ProductDefinition.Os.linux, vmargslinuxquery.getSingleResult());
    result.addVmArgs(ProductDefinition.Os.macosx, vmargsmacquery.getSingleResult());
    result.addVmArgs(ProductDefinition.Os.solaris, vmargssolarisquery.getSingleResult());
    result.addVmArgs(ProductDefinition.Os.win32, vmargswinquery.getSingleResult());

    result.addVm(ProductDefinition.Os.linux, vmlinuxquery.getSingleResult());
    result.addVm(ProductDefinition.Os.macosx, vmmacosxquery.getSingleResult());
    result.addVm(ProductDefinition.Os.solaris, vmsolarisquery.getSingleResult());
    result.addVm(ProductDefinition.Os.win32, vmwin32query.getSingleResult());

    String[] pluginids = pluginidquery.getResult();
    String[] isfragment = fragmentquery.getResult();
    for (int i = 0; i < pluginids.length; i++) {
      result.addPlugin(pluginids[i], Boolean.parseBoolean(isfragment[i]));
    }

    String[] featureids = featureidquery.getResult();
    String[] featureversions = featureversionquery.getResult();
    for (int i = 0; i < featureids.length; i++) {
      result.addFeature(featureids[i], new Version(featureversions[i]));
    }

    return result;

  }

} /* ENDCLASS */
