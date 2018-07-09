/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform;

import org.matrix.framework.core.database.manager.SqlMappingRegistry;
import org.matrix.framework.core.platform.im4j.service.MatrixIm4jService;
import org.matrix.framework.core.platform.im4j.service.manager.ThumbnailManager;
import org.matrix.framework.core.platform.upload.UploadFactory;
import org.matrix.framework.core.platform.web.freemarker.template.FreemarkerTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public abstract class MatrixAppConfig extends DefaultAppConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
    
    @Bean
    public ThumbnailManager thumbnailManager() {
        return new ThumbnailManager();
    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MatrixIm4jService matrixIm4jService() {
        return new MatrixIm4jService();
    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FreemarkerTemplate freemarkerTemplate() {
        return new FreemarkerTemplate();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public UploadFactory uploadFactory() {
        return new UploadFactory();
    }

    protected abstract void registrySqlMapping(SqlMappingRegistry registry);

}