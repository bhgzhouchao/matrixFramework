/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.util;

import org.junit.Assert;
import org.junit.Test;

public class ConvertToEnumTest {
    @Test
    public void convertEmptyToEnum() {
        TestEnum value = Convert.toEnum("", TestEnum.class, null);

        Assert.assertNull(value);
    }

    @Test
    public void convertValidNameToEnum() {
        TestEnum value = Convert.toEnum("A", TestEnum.class, null);

        Assert.assertEquals(TestEnum.A, value);
    }

    @Test
    public void convertInValidNameToEnum() {
        TestEnum value = Convert.toEnum("C", TestEnum.class, null);

        Assert.assertNull(value);
    }

    public static enum TestEnum {
        A, B
    }
}
