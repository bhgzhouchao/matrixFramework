package org.matrix.framework.core.platform.web.velocity;

import java.util.Map;
import javax.inject.Inject;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class VelocityAccess {

    private VelocityEngine velocityEngine;

    public String getEscapeHtml(String templateName, Map<String, Object> parematerMap) {
        final String velocityTemplate = templateName + ".vm";
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, velocityTemplate, parematerMap); 
    }
    
    @Inject
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
}