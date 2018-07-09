/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform;

import org.matrix.framework.core.SpringTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.CacheManager;

import javax.inject.Inject;

public class CacheTest extends SpringTest {
    @Inject
    CacheTestRepository repository;

    @Inject
    CacheManager cacheManager;

    @Test
    public void retrieveFromCache() {
        String key = "key";
        CacheTestRepository.Entity entity1 = repository.getEntityById(key);
        CacheTestRepository.Entity entity2 = repository.getEntityById(key);

        Assert.assertSame(entity1, entity2);
    }

    @Test
    public void evictByCacheManager() {
        String key = "key";
        CacheTestRepository.Entity entity1 = repository.getEntityById(key);

        cacheManager.getCache("entities").evict("key");

        CacheTestRepository.Entity entity2 = repository.getEntityById(key);

        Assert.assertNotSame(entity1, entity2);
    }
}
