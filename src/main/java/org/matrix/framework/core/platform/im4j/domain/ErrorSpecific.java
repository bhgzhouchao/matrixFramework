/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 * 这个类有bug会导致内存泄露，需要重写hashcode和equals方法
 **********************************************************************/
package org.matrix.framework.core.platform.im4j.domain;

public class ErrorSpecific {

    private String errorMessage;

    private ImgSpecific imgSpecific;

    public ErrorSpecific() {
    }

    public ErrorSpecific(String errorMessage, ImgSpecific imgSpecific) {
        this.errorMessage = errorMessage;
        this.imgSpecific = imgSpecific;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ImgSpecific getImgSpecific() {
        return imgSpecific;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setImgSpecific(ImgSpecific imgSpecific) {
        this.imgSpecific = imgSpecific;
    }

}