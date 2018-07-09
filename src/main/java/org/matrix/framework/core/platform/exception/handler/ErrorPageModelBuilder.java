/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.exception.handler;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import org.matrix.framework.core.settings.DeploymentSettings;

public class ErrorPageModelBuilder {

    private DeploymentSettings deploymentSettings;

    public Map<String, Object> buildErrorPageModel(Throwable exception) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("exception", new ExceptionDetail(exception));
        model.put("deploymentSettings", deploymentSettings);
        return model;
    }

    @Inject
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }
}
