package verificationcode;

import org.apache.commons.codec.binary.Base64;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangpeng on 2018/11/27.
 */
public class ImageUtil {

    public static BufferedImage base642BufferedImage(String base64) {
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
        }
        return img;
    }

    public static Mat base642MatNew(String base64) {
        Mat res = null;
        try {
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64);
            if (imageBytes != null) {
                res = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
            }
        } catch (Exception e) {
        }
        return res;
    }

    public static BufferedImage mat2BufferedImage(Mat input) {
        // this file extension
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", input, mob);
        // convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }

    public static String mat2Base64(Mat input) {
        BufferedImage image = mat2BufferedImage(input);
        try {
            if (image != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(image, "png", output);
                return Base64.encodeBase64String(output.toByteArray());
            }
        } catch (IOException ex) {
        }
        return null;
    }

    public static String bufferedImage2Base64(BufferedImage img, String format) {
        String base64 = null;
        try {
            if (img != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(img, format, os);
                base64 = Base64.encodeBase64String(os.toByteArray());
            }
        } catch (IOException e) {
        }
        return base64;
    }


    public static int[] binaryProjection(Mat binary, boolean xAxis) {
        return binaryProjection(binary, 0, binary.width(), 0, binary.height(), xAxis);
    }

    /**
     * 二值图投影
     *
     * @param binary 二值图
     * @param startX 投影x起点
     * @param endX   投影x终点
     * @param startY 投影y起点
     * @param endY   投影y终点
     * @param xAxis  x or y
     * @return 投影值
     */
    public static int[] binaryProjection(Mat binary, int startX, int endX, int startY, int endY, boolean xAxis) {
        if (binary == null) {
            return new int[]{};
        }
        startX = startX >= 0 ? startX : 0;
        startY = startY >= 0 ? startY : 0;
        endX = endX <= binary.width() ? endX : binary.width();
        endY = endY <= binary.height() ? endY : binary.height();

        int[] result = new int[xAxis ? (endX - startX) : (endY - startY)];
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                if (binary.get(j, i)[0] == 0x00) {
                    if (xAxis) {
                        //如果是X轴投影
                        result[i - startX]++;
                    } else {
                        //如果是Y轴投影
                        result[j - startY]++;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 投影值分组
     *
     * @param result
     * @param minPixelNum 最小像素累计值
     * @param minSpace    最小间隔数
     * @return
     */
    public static List<int[]> groupResult(int[] result, int minPixelNum, int minSpace) {
        List<int[]> bwY = new ArrayList<>();   // startY  endY
        if (result.length > 0) {
            int targetStart = -1;
            int cal = 0;
            for (int i = 0; i < result.length; i++) {
                if (targetStart == -1 && result[i] > minPixelNum) {
                    targetStart = i;
                } else if (targetStart != -1) {
                    if (result[i] <= minPixelNum) {
                        if (cal > minSpace) {
                            bwY.add(new int[]{targetStart, i - cal});
                            targetStart = -1;
                            cal = 0;
                        } else {
                            cal++;
                        }
                    } else {
                        cal = 0;
                    }
                }
            }
            if (targetStart != -1 && cal <= minSpace) {
                bwY.add(new int[]{targetStart, result.length - 1});
            }
        }
        return bwY;
    }

    public static Mat submat(Mat mat, Rect rect) {
        if (rect.x < 0 || rect.y < 0 || rect.width <= 0 || rect.height <= 0
                || rect.x > mat.width() || rect.x + rect.width > mat.width()
                || rect.y > mat.height() || rect.y + rect.height > mat.height()) {
            return null;
        }
        return mat.submat(rect);
    }

    /**
     * 二值化
     */
    public static Mat getBinaryImg(Mat img) {
        Mat binaryImg = img.clone();
        Imgproc.cvtColor(img, binaryImg, Imgproc.COLOR_RGB2GRAY);
        int h = img.height();
        if (img.height() % 2 != 1) {
            h = img.height() - 1;
        }
        int w = img.width();
        if (img.width() % 2 != 1) {
            w = img.width() - 1;
        }
        int blockSize = Math.max(w, h);
        double stdevVal = (double) blockSize / 6;
        return getBinaryImg(img, stdevVal);
    }

    /**
     * 二值化
     */
    public static Mat getBinaryImg(Mat img, double stdevVal) {
        Mat binaryImg = img.clone();
        Mat binaryImg1 = img.clone();
        Imgproc.cvtColor(img, binaryImg, Imgproc.COLOR_RGB2GRAY);
        int h = img.height();
        if (img.height() % 2 != 1) {
            h = img.height() - 1;
        }
        int w = img.width();
        if (img.width() % 2 != 1) {
            w = img.width() - 1;
        }
        int blockSize = Math.max(w, h);
        Imgproc.adaptiveThreshold(binaryImg, binaryImg1, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blockSize, stdevVal);
        return binaryImg1;
    }

    /**
     * 顺时针90度旋转
     *
     * @param origin
     * @return
     */
    public static Mat inversionNineTyRotation(Mat origin) {
        if (origin.empty()) {
            return null;
        }
        Mat res = new Mat();
        Core.transpose(origin, res);
        Core.flip(res, res, 1);
        return res;
    }

    /**
     * 180度旋转
     *
     * @param origin
     * @return
     */
    public static Mat inversionRotation(Mat origin) {
        if (origin.empty()) {
            return null;
        }
        Mat res = new Mat();
        Core.flip(origin, res, -1);
        return res;
    }

    /**
     * 旋转任意角度
     *
     * @param matImg
     * @param angel
     * @param format
     * @return
     */
    public static Mat getRotateImage(Mat matImg, double angel, String format) {
        Image src = mat2BufferedImage(matImg);
        BufferedImage des = rotateToBufferImg(src, angel);
        if (des == null) {
            return matImg;
        }
        String base64 = bufferedImage2Base64(des, format);
        Mat result = base642MatNew(base64);
        return result;
    }

    public static BufferedImage rotateToBufferImg(Image src, double angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        if (rect_des.height <= 0 || rect_des.width <= 0) {
            return null;
        }
        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < res.getWidth(); i++) {
            for (int j = 0; j < res.getHeight(); j++) {
                res.setRGB(i, j, 0xffffffff);
            }
        }
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), (double) src_width / 2, (double) src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }


    public static Rectangle calcRotatedSize(Rectangle src, double angel) {
        int w = src.width;
        int h = src.height;
        double angelPI = (angel * Math.PI) / 180;
        int des_width = (int) Math.ceil(w * Math.cos(angelPI) + h * Math.sin(angelPI));
        int des_height = (int) Math.ceil(w * Math.sin(angelPI) + h * Math.cos(angelPI));
        return new Rectangle(new Dimension(des_width, des_height));
    }


}
