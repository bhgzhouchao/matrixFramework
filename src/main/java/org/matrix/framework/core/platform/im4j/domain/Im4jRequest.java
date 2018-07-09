/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 * 这个类有bug会导致内存泄露，需要重写hashcode和equals方法
 **********************************************************************/
package org.matrix.framework.core.platform.im4j.domain;

import java.util.List;

public class Im4jRequest {

    // 原始图片地址
    private String originalImgPath;

    private String originalImgPrefix;

    private String originalImgSuffix;

    // 图片密度
    private double pdi; // 0.0 ~ 100.0

    // 图片规格
    private List<ImgSpecific> imgSpecifics;

    public String getOriginalImgPath() {
        return originalImgPath;
    }

    public void setOriginalImgPath(String originalImgPath) {
        this.originalImgPath = originalImgPath;
    }

    public List<ImgSpecific> getImgSpecifics() {
        return imgSpecifics;
    }

    public void setImgSpecifics(List<ImgSpecific> imgSpecifics) {
        this.imgSpecifics = imgSpecifics;
    }

    public double getPdi() {
        return pdi;
    }

    public void setPdi(double pdi) {
        this.pdi = pdi;
    }

    public String getOriginalImgPrefix() {
        return originalImgPrefix;
    }

    public void setOriginalImgPrefix(String originalImgPrefix) {
        this.originalImgPrefix = originalImgPrefix;
    }

    public String getOriginalImgSuffix() {
        return originalImgSuffix;
    }

    public void setOriginalImgSuffix(String originalImgSuffix) {
        this.originalImgSuffix = originalImgSuffix;
    }

}