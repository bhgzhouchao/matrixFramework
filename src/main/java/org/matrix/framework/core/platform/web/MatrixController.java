/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.matrix.framework.core.platform.exception.ResourceNotFoundException;
import org.matrix.framework.core.platform.exception.SessionTimeOutException;
import org.matrix.framework.core.platform.exception.UserAuthorizationException;
import org.matrix.framework.core.platform.exception.handler.ErrorPageModelBuilder;
import org.matrix.framework.core.settings.SiteSettings;

public class MatrixController implements SpringController {

    private SiteSettings siteSettings;

    private ErrorPageModelBuilder errorPageModelBuilder;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView error(Throwable exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getErrorPage(), model);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(ResourceNotFoundException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getResourceNotFoundPage(), model);
    }

    @ExceptionHandler(SessionTimeOutException.class)
    public ModelAndView sessionTimeOut(SessionTimeOutException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getSessionTimeOutPage(), model);
    }

    @ExceptionHandler(UserAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView unauthorized(UserAuthorizationException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView("default-403", model);
    }

    public boolean validateImageFile(List<MultipartFile> imageFiles) {
        for (MultipartFile imageFile : imageFiles) {
            boolean result = false;
            String contentType = imageFile.getContentType();
            for (String ct : new String[] { "image/jpeg", "image/pjpeg", "image/x-png", "image/gif", "image/png" }) {
                if (contentType.equalsIgnoreCase(ct.trim())) {
                    result = true;
                    break;
                }
            }
            if (!result)
                return false;
        }
        return true;
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setErrorPageModelBuilder(ErrorPageModelBuilder errorPageModelBuilder) {
        this.errorPageModelBuilder = errorPageModelBuilder;
    }

}