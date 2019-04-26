package verificationcode;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by jiangpeng on 2018/11/16.
 */
public class TestOCR {

    static {
        //使用三方打包好的自适应系统load opencv动态链接库的包
        nu.pattern.OpenCV.loadShared();
    }

    @Test
    public void testOCR() throws Exception {
        for (int i = 1; i <= 20; i++) {
            DebugUtil.inputPicName = String.valueOf(i);

            String imgPath = this.getClass().getClassLoader().getResource("verificationcode/" + DebugUtil.inputPicName + DebugUtil.inputPicSuffix).getPath();
            imgPath = imgPath.substring(1, imgPath.length());
            Mat origin = Imgcodecs.imread(imgPath);

            new VerficationCodeOCR().doOCR(origin);

        }
    }


}
