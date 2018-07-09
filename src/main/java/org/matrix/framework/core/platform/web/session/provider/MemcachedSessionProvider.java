/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.session.provider;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;

import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import org.matrix.framework.core.database.memcached.MatrixMemcachedFactory;
import org.matrix.framework.core.database.memcached.MatrixMemcachedGroup;
import org.matrix.framework.core.log.LoggerFactory;
import org.matrix.framework.core.platform.exception.MatrixException;
import org.matrix.framework.core.platform.monitor.ServiceStatus;
import org.matrix.framework.core.settings.SiteSettings;
import org.matrix.framework.core.util.ExceptionUtils;
import org.matrix.framework.core.util.TimeLength;

public class MemcachedSessionProvider implements SessionProvider {

    protected static final TimeLength MEMCACHED_TIME_OUT = TimeLength.seconds(3);

    private Map<String, MemcachedClient> memcachedClientMap = new ConcurrentHashMap<String, MemcachedClient>();

    private Map<String, String> memcachedServerMap = new ConcurrentHashMap<String, String>();

    private String servers;

    private SiteSettings siteSettings;

    @Override
    public String getAndRefreshSession(String groupName, String sessionId) {
        return getAndRefreshSession(groupName, sessionId, 0);
    }

    private String getAndRefreshSession(String groupName, String sessionId, int counter) {
        String sessionKey = getCacheKey(sessionId);
        CASValue<Object> cacheValue = null;
        try {            
            cacheValue = getClient(groupName).getAndTouch(sessionKey, expirationTime());
        } catch (Exception e) {
            if (counter > 3) {
                LoggerFactory.MEMCACHEDLOGGER.getLogger().error("MEMCACHED SERVERS:[" + memcachedServerMap.get(groupName) + "], getAndRefreshSession operition try 3 times failed;" + ExceptionUtils.stackTrace(e));
                throw new MatrixException("Operition memcached timeout,detail in log file.");
            }
            return getAndRefreshSession(groupName, sessionId, counter + 1);

        }
        if (cacheValue == null) {
            LoggerFactory.MEMCACHEDLOGGER.getLogger().warn("can not find session or session expired, sessionKey=" + sessionKey);
            return null;
        }
        return (String) cacheValue.getValue();
    }

    @Override
    public void saveSession(String groupName, String sessionId, String sessionData) {
        saveSession(groupName, sessionId, sessionData, 0);
    }

    private void saveSession(String groupName, String sessionId, String sessionData, int counter) {
        try {
            getClient(groupName).set(getCacheKey(sessionId), expirationTime(), sessionData);
        } catch (Exception e) {
            if (counter > 3) {
                LoggerFactory.MEMCACHEDLOGGER.getLogger().error("MEMCACHED SERVERS:[" + servers + "], saveSession operition try 3 times failed;" + ExceptionUtils.stackTrace(e));
                throw new MatrixException("Operition memcached timeout,detail in log file.");
            }
            saveSession(groupName, sessionId, sessionData, counter + 1);
        }
    }

    @Override
    public void clearSession(String groupName, String sessionId) {
        clearSession(groupName, sessionId, 0);
    }

    public void clearSession(String groupNameme, String sessionId, int counter) {
        try {
            getClient(groupNameme).delete(getCacheKey(sessionId));
        } catch (Exception e) {
            if (counter > 3) {
                LoggerFactory.MEMCACHEDLOGGER.getLogger().error("MEMCACHED SERVERS:[" + servers + "], clearSession operition try 3 times failed;" + ExceptionUtils.stackTrace(e));
                throw new MatrixException("Operition memcached timeout,detail in log file.");
            }
            clearSession(groupNameme, sessionId, counter + 1);
        }
    }

    @Override
    public String getServiceName() {
        return "MemcachedSession";
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        Collection<SocketAddress> availableServers = getClient(MatrixMemcachedGroup.SESSIONGROUP).getAvailableServers();
        return availableServers.isEmpty() ? ServiceStatus.DOWN : ServiceStatus.UP;
    }

    // TODO: use different namespace for secure session?
    private String getCacheKey(String sessionId) {
        return "session:" + sessionId;
    }

    private int expirationTime() {
        return (int) siteSettings.getSessionTimeOut().toSeconds();
    }

    public MemcachedClient getClient(String groupName) {
        return memcachedClientMap.get(groupName);
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setMatrixMemcachedFactory(MatrixMemcachedFactory matrixMemcachedFactory) {
        memcachedClientMap.put(MatrixMemcachedGroup.SESSIONGROUP, matrixMemcachedFactory.getClient(MatrixMemcachedGroup.SESSIONGROUP));
        final MemcachedClient secondMemcachedClient = matrixMemcachedFactory.getClient(MatrixMemcachedGroup.SECOND_SESSIONGROUP);
        if (null != secondMemcachedClient) memcachedClientMap.put(MatrixMemcachedGroup.SECOND_SESSIONGROUP, secondMemcachedClient);

        memcachedServerMap.put(MatrixMemcachedGroup.SESSIONGROUP, matrixMemcachedFactory.getServers(MatrixMemcachedGroup.SESSIONGROUP));
        final String secondServers = matrixMemcachedFactory.getServers(MatrixMemcachedGroup.SECOND_SESSIONGROUP);
        if (null != secondServers) memcachedServerMap.put(MatrixMemcachedGroup.SECOND_SESSIONGROUP, secondServers);
    }

}