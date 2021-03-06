/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.cookie;

import org.matrix.framework.core.util.TimeLength;

public class CookieSpec {
    public static final TimeLength MAX_AGE_SESSION_SCOPE = TimeLength.seconds(-1);
    public static final TimeLength ONE_YEAR = TimeLength.days(365);

    private String path;
    private Boolean httpOnly;
    private Boolean secure;
    private TimeLength maxAge;
    private String domain;

    public CookieSpec() {
        super();
    }

    public CookieSpec(TimeLength maxAge) {
        super();
        this.maxAge = maxAge;
        this.path = "/";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public TimeLength getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(TimeLength maxAge) {
        this.maxAge = maxAge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
