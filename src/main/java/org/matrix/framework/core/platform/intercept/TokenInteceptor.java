/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.intercept;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.matrix.framework.core.collection.Key;
import org.matrix.framework.core.collection.converter.JSONConverter;
import org.matrix.framework.core.http.HTTPConstants;
import org.matrix.framework.core.platform.web.freemarker.tag.TokenTag;
import org.matrix.framework.core.platform.web.session.SessionContext;
import org.matrix.framework.core.platform.web.site.URLBuilder;

public class TokenInteceptor  extends HandlerInterceptorAdapter {
    
    private static final Integer SYNCH_TOKEN_AGE = 30;

    private SessionContext sessionContext;
    
    private JSONConverter jsonConvert;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod && isDuplicateFormSubmission(request)) {
            redirectToTokenExpiredPage(request, response);
            return false; 
        }
        return true;
    }
    
    public boolean isDuplicateFormSubmission(HttpServletRequest request) {
        String token = request.getParameter(TokenTag.SYNCH_TOKEN_INPUT_FIELD_NAME);
        boolean isDuplicate = false;
        if (StringUtils.hasText(token)) {
            Map<String, Long> synchTokens = null;
            String mapString = sessionContext.get(Key.stringKey(TokenTag.SYNCH_TOKEN_INPUT_FIELD_NAME));
            if (StringUtils.hasText(mapString)) {
                synchTokens = jsonConvert.fromString(Map.class, mapString, String.class, Long.class);
            }
            if (null == synchTokens) {
                synchTokens = new HashMap<String, Long>();    
            }
            if (!synchTokens.containsKey(token)) {
                long now = System.currentTimeMillis();    //curent time
                long tooOld = now - SYNCH_TOKEN_AGE;      //now -30s
                
                Set<Entry<String, Long>> oldTokens = synchTokens.entrySet();
                Iterator<Entry<String, Long>> iter = oldTokens.iterator();
                while (iter.hasNext()) {
                    Entry<String, Long> oldToken = iter.next();
                    long submitTime = oldToken.getValue();
                    while (submitTime <= tooOld) {
                        iter.remove();
                        break;
                    }
                }
                synchTokens.put(token, now);
            } else {
                isDuplicate = true;
            }
            sessionContext.set(Key.stringKey(TokenTag.SYNCH_TOKEN_INPUT_FIELD_NAME), jsonConvert.toString(synchTokens));
        }
        return isDuplicate;
    }
    
    private void redirectToTokenExpiredPage(HttpServletRequest request, HttpServletResponse response) {
        URLBuilder builder = new URLBuilder();
        builder.setContext(request.getContextPath(), null);
        builder.setLogicalURL("/duplicate-submit");
        response.setStatus(HTTPConstants.SC_MOVED_TEMPORARILY);
        response.setHeader(HTTPConstants.HEADER_REDIRECT_LOCATION, builder.buildRelativeURL());
    }

    @Inject
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Inject
    public void setJsonConvert(JSONConverter jsonConvert) {
        this.jsonConvert = jsonConvert;
    }

}