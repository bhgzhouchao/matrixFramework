/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 * 这个类有bug会导致内存泄露，需要重写hashcode和equals方法
 **********************************************************************/
package org.matrix.framework.core.platform.im4j.domain;

import java.util.ArrayList;
import java.util.List;

public class Im4jBulkRequest {

    private final List<Im4jRequest> im4jBulkRequest = new ArrayList<Im4jRequest>();

    public List<Im4jRequest> getBulkRequest() {
        return im4jBulkRequest;
    }
}