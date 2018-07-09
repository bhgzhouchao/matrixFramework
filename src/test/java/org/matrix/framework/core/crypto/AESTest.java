/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.crypto;

import org.matrix.framework.core.util.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class AESTest {
    @Test
    public void encryptAndDecrypt() throws NoSuchAlgorithmException {
        AES aes = new AES();

        byte[] key = aes.generateKey();
        System.err.println(DigestUtils.base64(key));

        aes.setKey(key);
        String message = "test-message";

        byte[] cipherText = aes.encrypt(message.getBytes());

        byte[] plainBytes = aes.decrypt(cipherText);

        String plainText = new String(plainBytes);

        Assert.assertEquals(message, plainText);
    }
}
