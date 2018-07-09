package org.matrix.framework.core.platform.upload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadFactory {

    public Map<String, UploadStrategy> uploadStrategyMap = new ConcurrentHashMap<String, UploadStrategy>();

    public void registUploadStrategy(String modelName , UploadStrategy uploadStrategy) {
        uploadStrategyMap.put(modelName, uploadStrategy);
    }

    public UploadStrategy getUploadStrategy(String modelName) {
        return uploadStrategyMap.get(modelName);
    }
}
