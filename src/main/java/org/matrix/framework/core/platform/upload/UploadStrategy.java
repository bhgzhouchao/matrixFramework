package org.matrix.framework.core.platform.upload;

import java.util.List;
import javax.inject.Inject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartFile;

public abstract class UploadStrategy {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public abstract <T> T upload(List<MultipartFile> imageFiles);

    public <T> void asyncUpload(final List<MultipartFile> imageFiles, final UploadCallBack... uploadCallBacks) {
        threadPoolTaskExecutor.execute(new AsyncUploadThread(this) {
            @Override
            public void run() {
                T response = this.uploadStrategy.upload(imageFiles);
                if (null != uploadCallBacks && uploadCallBacks.length != 0) {
                    uploadCallBacks[0].call(response);
                }
            }
        });
    }

    public interface UploadCallBack {
        public <T> void call(T t);
    }

    private abstract class AsyncUploadThread implements Runnable {

        public final UploadStrategy uploadStrategy;
        
        public AsyncUploadThread(UploadStrategy uploadStrategy) {
            this.uploadStrategy = uploadStrategy;
        }
    }

    @Inject
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

}