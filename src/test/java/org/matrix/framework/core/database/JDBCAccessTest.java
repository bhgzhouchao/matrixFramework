/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.database;

import org.matrix.framework.core.SpringTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class JDBCAccessTest extends SpringTest {
    
    @Inject
    JDBCAccess jdbcAccess;

    @Before
    public void createTestTable() {
        jdbcAccess.execute("junit.create");
        jdbcAccess.execute("junit.save", "value");
    }

    @After
    public void dropTestTable() {
        jdbcAccess.execute("junit.drop");
    }

    @Test
    public void findStringBySQL() {
        String value = jdbcAccess.findString("junit.query");

        Assert.assertEquals("value", value);
    }

    @Test
    public void findIntegerBySQL() {
        Integer count = jdbcAccess.findInteger("junit.cquery");

        Assert.assertEquals(1, (int) count);
    }
}
