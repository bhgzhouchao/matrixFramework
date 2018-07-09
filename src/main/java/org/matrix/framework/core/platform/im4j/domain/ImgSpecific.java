/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 * 这个类有bug会导致内存泄露，需要重写hashcode和equals方法
 **********************************************************************/
package org.matrix.framework.core.platform.im4j.domain;

import java.io.Serializable;

/**
 * 图片规格
 */
public class ImgSpecific implements Serializable {

    private String imgType;

    private long width;

    private long height;

    public ImgSpecific() {
    }

    public ImgSpecific(long width, long height) {
        this.width = width;
        this.height = height;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

}