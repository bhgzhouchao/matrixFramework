/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.crypto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MD5Test {

    MD5 md5;

    @Before
    public void createMD5() {
        md5 = new MD5();
    }

    @Test
    public void encrypt() {
        Assert.assertEquals(md5.encrypt("userId=DAT&userPass=888888&orderNo=20081231-1000&ticketCount=2&cinemaId=12345678&cinemaLinkId=999&randKey=12345678"), "33612081875988FEB74A9CCAC4C5372E");
    }

}