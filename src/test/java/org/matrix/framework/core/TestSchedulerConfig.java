/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core;

import org.matrix.framework.core.platform.DefaultSchedulerConfig;
import org.matrix.framework.core.platform.JobRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSchedulerConfig extends DefaultSchedulerConfig {
    @Override
    protected void configure(JobRegistry registry) {
    }
}
