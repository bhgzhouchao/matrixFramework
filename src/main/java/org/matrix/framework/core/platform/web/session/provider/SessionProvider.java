/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.session.provider;

import org.matrix.framework.core.platform.monitor.ServiceMonitor;

public interface SessionProvider extends ServiceMonitor {

    String getAndRefreshSession(String groupName, String sessionId);

    void saveSession(String groupName, String sessionId, String sessionData);

    void clearSession(String groupName, String sessionId);
}
