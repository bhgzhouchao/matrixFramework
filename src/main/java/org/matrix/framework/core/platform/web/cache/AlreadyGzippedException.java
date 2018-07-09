/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.cache;

import net.sf.ehcache.CacheException;

public class AlreadyGzippedException extends CacheException {

    public AlreadyGzippedException() {
        super();
    }

    public AlreadyGzippedException(String message) {
        super(message);
    }
    
}