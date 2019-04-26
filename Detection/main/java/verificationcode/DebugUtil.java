package verificationcode;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jiangpeng on 2018/11/16.
 */
public class DebugUtil {
    public static boolean debugSwitch = true;

    public static String testPath = "D:\\OCR\\verificationcode\\result";  //输出目录
    public static String outputPicSuffix = ".jpg";  //输出图片后缀
    public static String inputPicName = "";  //输入图片名称
    public static String inputPicSuffix = ".png";  //输入图片类型

    /**
     * 保存测试过程的图片到本地
     *
     * @param mat           图片
     * @param outputPicName 文件名
     * @param debug         是否debug
     */
    public static void saveDebugPic(Mat mat, String outputPicName, boolean debug) {
        try {
            if (!debug) {
                return;
            }
            createDirectory(testPath);
            Mat clone = mat.clone();
            StringBuilder fullPicName = new StringBuilder();
            fullPicName.append(testPath).append(File.separator);
            fullPicName.append(getDebugPicNamePre() + outputPicName + outputPicSuffix);
            Imgcodecs.imwrite(fullPicName.toString(), clone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveDebugPic(Mat mat, String outputPicName) {
        saveDebugPic(mat, outputPicName, debugSwitch);
    }

    /**
     * @return 图片名称前缀
     */
    public static String getDebugPicNamePre() {
        String namePre = inputPicName + "-";
        return namePre;
    }

    /**
     * 创建目录
     */
    public static void createDirectory(String path) {
        try {
            if (!debugSwitch) {
                return;
            }
            File file = new File(path.toString());//目录全路径
            if (file.exists()) {
                return;
            }
            String[] paths = {};
            try {
                String tempPath = new File(path).getCanonicalPath();
                paths = tempPath.split("\\\\");//windows
                if (paths.length == 1) {
                    paths = tempPath.split("/");
                }//linux
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder fullPath = new StringBuilder();
            for (String p : paths) {
                fullPath.append(p).append(File.separator);
                file = new File(fullPath.toString());
                if (!file.exists()) {
                    file.mkdir();
                }
            }
            file = new File(fullPath.toString());//目录全路径
            if (!file.exists()) {
                System.out.println("创建目录失败。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
