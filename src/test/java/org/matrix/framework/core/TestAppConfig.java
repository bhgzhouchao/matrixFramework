/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core;

import org.matrix.framework.core.database.JDBCAccess;
import org.matrix.framework.core.database.manager.SqlMappingRegistry;
import org.matrix.framework.core.platform.MatrixScopeResolver;
import org.matrix.framework.core.platform.DefaultAppConfig;
import org.matrix.framework.core.platform.concurrent.TaskExecutor;
import org.matrix.framework.core.platform.intercept.AnnotatedMethodAdvisor;
import org.matrix.framework.core.platform.intercept.InterceptorTestAnnotation;
import org.matrix.framework.core.platform.intercept.TestInterceptor;
import org.matrix.framework.core.util.ClasspathResource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = TestAppConfig.class, scopeResolver = MatrixScopeResolver.class)
@EnableCaching
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class TestAppConfig extends DefaultAppConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }
    
    @Bean
    public JDBCAccess jdbcAccess() {
        JDBCAccess jdbcAccess = new JDBCAccess();
        jdbcAccess.setDataSource(dataSource());
        jdbcAccess.setSqlManager(sqlManager());
        return jdbcAccess;
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager manager = new EhCacheCacheManager();
        manager.setCacheManager(new net.sf.ehcache.CacheManager(new ClasspathResource("ehcache.xml").getInputStream()));
        return manager;
    }
    
   
    @Bean
    public TaskExecutor taskExecutor() {
        return new TaskExecutor(1);
    }

    @Bean
    public AnnotatedMethodAdvisor testInterceptor() {
        return new AnnotatedMethodAdvisor(InterceptorTestAnnotation.class, TestInterceptor.class);
    }

    @Override
    public void registrySqlMapping(SqlMappingRegistry registry) {
        registry.registSqlMapping("sqlmapping.xml"); 
    }

}