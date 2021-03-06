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
package org.ant4eclipse.lib.pde.model.product;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.tools.BundleStartRecord;
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
  public static final ProductDefinition parseProductDefinition(InputStream inputstream) {

    Assure.notNull("inputstream", inputstream);

    XQueryHandler queryhandler = new XQueryHandler();

    XQuery namequery = queryhandler.createQuery("/product/@name");
    XQuery uidquery = queryhandler.createQuery("/product/@uid");
    XQuery idquery = queryhandler.createQuery("/product/@id");
    XQuery applicationquery = queryhandler.createQuery("/product/@application");
    XQuery versionquery = queryhandler.createQuery("/product/@version");
    XQuery usefeaturesquery = queryhandler.createQuery("/product/@useFeatures");

    XQuery launchernamequery = queryhandler.createQuery("/product/launcher/@name");
    XQuery splashquery = queryhandler.createQuery("/product/splash/@location");

    XQuery pluginidquery = queryhandler.createQuery("/product/plugins/plugin/@id");
    XQuery fragmentquery = queryhandler.createQuery("/product/plugins/plugin/@fragment");

    XQuery featureidquery = queryhandler.createQuery("/product/features/feature/@id");
    XQuery featureversionquery = queryhandler.createQuery("/product/features/feature/@version");

    XQuery configinilinuxquery = queryhandler.createQuery("/product/configIni/linux");
    XQuery configinimacosxquery = queryhandler.createQuery("/product/configIni/macosx");
    XQuery configinisolarisquery = queryhandler.createQuery("/product/configIni/solaris");
    XQuery configiniwin32query = queryhandler.createQuery("/product/configIni/win32");

    XQuery programallquery = queryhandler.createQuery("/product/launcherArgs/programArgs");
    XQuery programlinuxquery = queryhandler.createQuery("/product/launcherArgs/programArgsLin");
    XQuery programmacquery = queryhandler.createQuery("/product/launcherArgs/programArgsMac");
    XQuery programsolarisquery = queryhandler.createQuery("/product/launcherArgs/programArgsSol");
    XQuery programwinquery = queryhandler.createQuery("/product/launcherArgs/programArgsWin");

    XQuery vmargsallquery = queryhandler.createQuery("/product/launcherArgs/vmArgs");
    XQuery vmargslinuxquery = queryhandler.createQuery("/product/launcherArgs/vmArgsLin");
    XQuery vmargsmacquery = queryhandler.createQuery("/product/launcherArgs/vmArgsMac");
    XQuery vmargssolarisquery = queryhandler.createQuery("/product/launcherArgs/vmArgsSol");
    XQuery vmargswinquery = queryhandler.createQuery("/product/launcherArgs/vmArgsWin");

    XQuery vmlinuxquery = queryhandler.createQuery("/product/vm/linux");
    XQuery vmmacosxquery = queryhandler.createQuery("/product/vm/macosx");
    XQuery vmsolarisquery = queryhandler.createQuery("/product/vm/solaris");
    XQuery vmwin32query = queryhandler.createQuery("/product/vm/win32");

    XQuery configidquery = queryhandler.createQuery("/product/configurations/plugin/@id");
    XQuery configstartquery = queryhandler.createQuery("/product/configurations/plugin/@autoStart");
    XQuery configlevelquery = queryhandler.createQuery("/product/configurations/plugin/@startLevel");

    XQueryHandler.queryInputStream(inputstream, queryhandler);

    ProductDefinition result = new ProductDefinition();

    result.setUid(uidquery.getSingleResult());
    result.setName(namequery.getSingleResult());
    String versionString = versionquery.getSingleResult();
    try {
      result.setVersion(new Version(versionString == null || "null".equals(versionString) ? "0.0.0" : versionString));
    } catch (Exception e) {
      throw new Ant4EclipseException(e, PdeExceptionCode.INVALID_CONFIGURATION_VALUE, versionString, "version");
    }

    result.setApplication(applicationquery.getSingleResult());
    result.setId(idquery.getSingleResult());

    result.setLaunchername(launchernamequery.getSingleResult());
    result.setSplashplugin(splashquery.getSingleResult());
    result.setBasedOnFeatures(Boolean.parseBoolean(usefeaturesquery.getSingleResult()));

    result.addConfigIni(ProductOs.linux, configinilinuxquery.getSingleResult());
    result.addConfigIni(ProductOs.macosx, configinimacosxquery.getSingleResult());
    result.addConfigIni(ProductOs.solaris, configinisolarisquery.getSingleResult());
    result.addConfigIni(ProductOs.win32, configiniwin32query.getSingleResult());

    String programarg = Utilities.cleanup(programallquery.getSingleResult());
    if (programarg != null) {
      for (ProductOs os : ProductOs.values()) {
        result.addProgramArgs(os, programarg);
      }
    }
    result.addProgramArgs(ProductOs.linux, programlinuxquery.getSingleResult());
    result.addProgramArgs(ProductOs.macosx, programmacquery.getSingleResult());
    result.addProgramArgs(ProductOs.solaris, programsolarisquery.getSingleResult());
    result.addProgramArgs(ProductOs.win32, programwinquery.getSingleResult());

    String vmarg = Utilities.cleanup(vmargsallquery.getSingleResult());
    if (vmarg != null) {
      for (ProductOs os : ProductOs.values()) {
        result.addVmArgs(os, vmarg);
      }
    }
    result.addVmArgs(ProductOs.linux, vmargslinuxquery.getSingleResult());
    result.addVmArgs(ProductOs.macosx, vmargsmacquery.getSingleResult());
    result.addVmArgs(ProductOs.solaris, vmargssolarisquery.getSingleResult());
    result.addVmArgs(ProductOs.win32, vmargswinquery.getSingleResult());

    result.addVm(ProductOs.linux, vmlinuxquery.getSingleResult());
    result.addVm(ProductOs.macosx, vmmacosxquery.getSingleResult());
    result.addVm(ProductOs.solaris, vmsolarisquery.getSingleResult());
    result.addVm(ProductOs.win32, vmwin32query.getSingleResult());

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

    String[] configids = configidquery.getResult();
    String[] configautostarts = configstartquery.getResult();
    String[] configlevels = configlevelquery.getResult();
    for (int i = 0; i < configids.length; i++) {
      BundleStartRecord record = new BundleStartRecord();
      record.setId(configids[i]);
      record.setAutoStart(Boolean.parseBoolean(configautostarts[i]));
      record.setStartLevel(Integer.parseInt(configlevels[i]));
      result.addConfigurationRecord(record);
    }

    validate(result);

    return result;

  }

  /**
   * Performs a simple validation of the data currently stored within this description.
   * 
   * @param result
   *          The product definition instance that needs to be validated.
   */
  private static final void validate(ProductDefinition result) {
    if ((result.getName() == null) || (result.getName().length() == 0)) {
      throw new Ant4EclipseException(PdeExceptionCode.INVALID_CONFIGURATION_VALUE, "", "name");
    }

    // [GERD - 22.07.2010] The product id is optional!
    // if ((result.getId() == null) || (result.getId().length() == 0)) {
    // throw new Ant4EclipseException(PdeExceptionCode.INVALID_CONFIGURATION_VALUE, "", "id");
    // }
    for (ProductOs os : ProductOs.values()) {
      String configini = result.getConfigIni(os);
      if (configini != null) {
        String name = String.format("configIni/%s", os.name());
        if (configini.length() == 0) {
          throw new Ant4EclipseException(PdeExceptionCode.INVALID_CONFIGURATION_VALUE, "", name);
        }
        if (configini.charAt(0) != '/') {
          // it's not starting with a slash indicating workspace relativity
          throw new Ant4EclipseException(PdeExceptionCode.INVALID_CONFIGURATION_VALUE, configini, name);
        }
        int next = configini.indexOf('/', 1);
        if (next == -1) {
          // it's not starting with a slash indicating workspace relativity
          throw new Ant4EclipseException(PdeExceptionCode.INVALID_CONFIGURATION_VALUE, configini, name);
        }
        if (next == configini.length() - 1) {
          // there's no file path after the project name
          throw new Ant4EclipseException(PdeExceptionCode.INVALID_CONFIGURATION_VALUE, configini, name);
        }
      }
    }
  }

} /* ENDCLASS */
