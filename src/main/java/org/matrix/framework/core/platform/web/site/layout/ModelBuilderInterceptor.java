/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.site.layout;

import org.slf4j.Logger;
import org.matrix.framework.core.log.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ModelBuilderInterceptor extends HandlerInterceptorAdapter {
    
    private final Logger logger = LoggerFactory.getLogger(ModelBuilderInterceptor.class);
    
    private final Map<Class<? extends Annotation>, ModelBuilder> modelBuilders = new HashMap<Class<? extends Annotation>, ModelBuilder>();

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!isTemplateView(modelAndView, handler)) return;

        Map<String, Object> model = modelAndView.getModel();
        Class<?> controllerClass = ((HandlerMethod) handler).getBeanType();
        
        for (Map.Entry<Class<? extends Annotation>, ModelBuilder> entry : modelBuilders.entrySet()) {
            Class<? extends Annotation> annotationClass = entry.getKey();
            if (isAnnotationPresentInClassHierarchy(controllerClass, annotationClass)) {
                ModelBuilder builder = entry.getValue();
                logger.debug("prepare model by model builder, annotation={}, builder={}", annotationClass.getName(), builder.getClass().getName());
                builder.build(model);
            }
        }
    }

    private boolean isTemplateView(ModelAndView modelAndView, Object handler) {
        if (modelAndView == null) return false; // for @ResponseBody
        String viewName = modelAndView.getViewName();
        if (viewName == null) return false;
        if (viewName.startsWith(UrlBasedViewResolver.FORWARD_URL_PREFIX)
                || viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)) return false; // forward and redirect
        if (!(handler instanceof HandlerMethod) || modelAndView.getModel() == null) return false;   // no model is injected to Controller
        return true;
    }

    private boolean isAnnotationPresentInClassHierarchy(Class<?> controllerClass, Class<? extends Annotation> annotationClass) {
        Class<?> targetClass = controllerClass;
        while (true) {
            if (targetClass.isAnnotationPresent(annotationClass)) return true;
            targetClass = targetClass.getSuperclass();
            if (Object.class.equals(targetClass)) return false;
        }
    }

    public void registerModelBuilder(Class<? extends Annotation> annotationClass, ModelBuilder testPageBuilder) {
        modelBuilders.put(annotationClass, testPageBuilder);
    }
    
}