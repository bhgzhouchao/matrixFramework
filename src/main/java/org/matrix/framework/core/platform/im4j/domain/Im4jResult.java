/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 * 这个类有bug会导致内存泄露，需要重写hashcode和equals方法
 **********************************************************************/
package org.matrix.framework.core.platform.im4j.domain;

import java.util.List;

public class Im4jResult {

    // 原始图片地址
    private String originalImgPath;

    // 图片规格
    private List<ErrorSpecific> errorSpecific;

    public String getOriginalImgPath() {
        return originalImgPath;
    }

    public List<ErrorSpecific> getErrorSpecific() {
        return errorSpecific;
    }

    public void setOriginalImgPath(String originalImgPath) {
        this.originalImgPath = originalImgPath;
    }

    public void setErrorSpecific(List<ErrorSpecific> errorSpecific) {
        this.errorSpecific = errorSpecific;
    }

}