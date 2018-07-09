/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.session;

public class SecureSessionContext extends SessionContext {
    
    protected boolean underSecureRequest;

    void underSecureRequest() {
        underSecureRequest = true;
    }

    public boolean isUnderSecureRequest() {
        return underSecureRequest;
    }

}