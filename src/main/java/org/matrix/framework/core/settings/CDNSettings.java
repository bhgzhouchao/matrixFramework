/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.settings;

public interface CDNSettings {
    
    String[] getCDNHosts();
    
    String[] getNFSHosts();
    
    String getLocalPath();

    boolean supportHTTPS();
    
    boolean supportS3();
    
    String getBucketName();
}
