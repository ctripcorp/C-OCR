package verificationcode;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;

/**
 * Created by jiangpeng on 2019/03/25.
 */
public class VerficationCodeOCR {

    public void doOCR(Mat origin) {
        if (origin == null || origin.empty()) {
            return;
        }
        List<CodeEntity> codeEntities = detection(origin);  //检测
        for (int i = 0; i < codeEntities.size(); i++) {
            CodeEntity codeEntity = codeEntities.get(i);
            Mat oper = codeEntity.getMat().clone();
            Imgproc.resize(oper, oper, new Size(40, 40));   //缩放成40*40，便于后续识别
            DebugUtil.saveDebugPic(oper, "code-" + i);
        }
    }

    /**
     * 检测
     */
    private List<CodeEntity> detection(Mat origin) {
        List<CodeEntity> codeEntities = new ArrayList<>();
        Mat copy = origin.clone();
        copy = threshold(copy);
        List<Rect> codes = cutCode(copy);

        List<Mat> cleanCode = cleanCode(copy, codes);
        for (int i = 0; i < 6; i++) {
            CodeEntity codeEntity = new CodeEntity();
            codeEntity.setRect(codes.get(i));
            codeEntity.setMat(cleanCode.get(i));
            codeEntity.setSequence(i);
            RotatedRect rotatedRect = getRotatedRect(cleanCode.get(i));
            codeEntity.setRotatedRect(rotatedRect);
            codeEntities.add(codeEntity);
        }
        return codeEntities;
    }

    /**
     * 清除除字符外的其他噪声
     * @param mat 验证码整图
     * @param codes 字符坐标
     * @return 清除噪声后的字符
     */
    private List<Mat> cleanCode(Mat mat, List<Rect> codes) {
        List<Mat> codeMat = new ArrayList<>();
        for (Rect rect : codes) {
            Mat code = ImageUtil.submat(mat, rect);
            //加一圈白边
            Mat copy = code.clone();
            Core.copyMakeBorder(copy, copy, 1, 1, 1, 1, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));
            codeMat.add(copy);
        }
        List<Mat> cleanCode = new ArrayList<>();
        for (Mat code : codeMat) {
            Mat oper = new Mat();
            Core.bitwise_not(code, oper);
            oper = new ConnectedDomainUtil().connectedDomain(oper);
            Core.bitwise_not(oper, oper);

            List<Rect> contourList = findContours(oper);
            Comparator<Rect> sortByAreaRev = Comparator.comparing(u -> -(u.width * u.height));
            contourList.sort(sortByAreaRev);

            oper = ImageUtil.submat(oper, contourList.get(0));
            cleanCode.add(oper);
        }
        return cleanCode;
    }

    /**
     * 根据字体与背景像素值的差异二值化
     */
    private Mat threshold(Mat mat) {
        if (mat == null) {
            return null;
        }
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

        //网格背景的像素值为211  字体 170
        Imgproc.threshold(mat, mat, 190, 255, Imgproc.THRESH_BINARY); //200
        return mat;
    }

    /**
     * 根据连通域分析获取6为字符的位置
     */
    private List<Rect> cutCode(Mat mat) {
        List<Rect> contourList = findContours(mat);
        Comparator<Rect> sortByAreaRev = Comparator.comparing(u -> -(u.width * u.height));
        contourList.sort(sortByAreaRev);
        contourList = contourList.subList(0, 6);

        Comparator<Rect> sortByX = Comparator.comparing(u -> u.x);
        contourList.sort(sortByX);

        return contourList;
    }

    /**
     * 获取连通域轮廓
     */
    private List<Rect> findContours(Mat mat) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE, new Point());

        List<Rect> contourList = new ArrayList<>();
        for (MatOfPoint point : contours) {
            Rect rect = Imgproc.boundingRect(point);
            contourList.add(rect);
        }
        return RectUtil.filterInnerRect(mat, contourList);
    }

    /**
     * 获取字符的最小外接矩形
     * 在后处理比较时用到
     * @param code 字符二值图
     */
    private RotatedRect getRotatedRect(Mat code) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(code, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE, new Point());
        List<MatOfPoint2f> newContours = new ArrayList<>();
        for (MatOfPoint point : contours) {
            MatOfPoint2f newPoint = new MatOfPoint2f(point.toArray());
            newContours.add(newPoint);
        }
        List<RotatedRect> contourList = new ArrayList<>();
        for (MatOfPoint2f point : newContours) {
            RotatedRect rotatedRect = Imgproc.minAreaRect(point);
            contourList.add(rotatedRect);
        }
        Comparator<RotatedRect> sortByArea = Comparator.comparing(u -> -u.size.area());
        contourList.sort(sortByArea);

        return contourList.get(1);
    }

}
