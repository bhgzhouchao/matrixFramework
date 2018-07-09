/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.scheduler;

import org.matrix.framework.core.SpringTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

public class SchedulerTest extends SpringTest {
    @Inject
    Scheduler scheduler;

    @Test
    public void schedulerShouldBeRegistered() {
        Assert.assertNotNull("Scheduler should be registered by DefaultSchedulerConfig", scheduler);
    }
}
