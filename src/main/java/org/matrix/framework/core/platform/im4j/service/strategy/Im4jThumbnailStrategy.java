package org.matrix.framework.core.platform.im4j.service.strategy;

import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;

public class Im4jThumbnailStrategy implements ThumbnailStrategy {

    @Override
    public void thumbnailImage(String originalImgPath, double imgPdi, long width, long height, String destImgPath) throws Exception {
        final GMOperation op = new GMOperation();
        op.addImage(originalImgPath);
        op.quality(imgPdi);
        op.addRawArgs("-resize", width + "x" + height);
        op.addRawArgs("-gravity", "center");
        op.addImage(destImgPath);
        ConvertCmd convert = new ConvertCmd(true);
        convert.run(op);
    }

}
