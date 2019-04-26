package verificationcode;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连通域分析
 * 获取目标连通域
 * 其他噪点置0
 * Created by jiangpeng on 2019/03/26.
 */
public class ConnectedDomainUtil {

    private static int maxValue = 255;

    /**
     * 二值图
     * 连通域 255
     * 背景 0
     */
    public Mat connectedDomain(Mat src) {
        if (src.empty()) {
            return null;
        }
        Mat oper = src.clone();
        List<Connected> list = ergodic(oper);
        return reset(oper, list);
    }

    /**
     * 保留目标连通域，其他连通域置0
     */
    private Mat reset(Mat mat, List<Connected> list) {
        Comparator<Connected> sortByHeight = Comparator.comparing(u -> -u.getRect().height);
        list.sort(sortByHeight);
        Connected target = list.get(0);
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                if (mat.get(i, j)[0] != target.getValue()) {
                    mat.put(i, j, 0);
                } else {
                    mat.put(i, j, maxValue);
                }
            }
        }
        return mat;
    }

    /**
     * 遍历图像，不同的联通域设置不同的值
     */
    private List<Connected> ergodic(Mat mat) {
        List<Connected> list = new ArrayList<>();
        Double key = 0D;
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                if (mat.get(i, j)[0] == maxValue) {
                    key++;
                    AtomicInteger number = new AtomicInteger(0);
                    AtomicInteger endi = new AtomicInteger(i);
                    AtomicInteger endj = new AtomicInteger(j);

                    erase(mat, i, j, key, number, endi, endj);

                    Rect rect = new Rect();
                    rect.x = j;
                    rect.y = i;
                    rect.width = endj.get() - j;
                    rect.height = endi.get() - i;
                    Connected connected = new Connected(key, number, rect);

                    list.add(connected);
                }
            }
        }
        return list;
    }

    /**
     * 递归分析连通域
     * @param mat 分析图
     * @param i 待分析坐标
     * @param j 待分析坐标
     * @param res 当前连通域的值置为 res
     * @param number 连通域累计像素点数
     * @param endi 连通域边界
     * @param endj 连通域边界
     */
    private void erase(Mat mat, int i, int j, double res, AtomicInteger number, AtomicInteger endi, AtomicInteger endj) {
        number.set(number.intValue() + 1); //计算连通域累计点数
        if (endi.get() < i) {
            endi.set(i);
        }
        if (endj.get() < j) {
            endj.set(j);
        }
        mat.put(i, j, res);
        while (i - 1 >= 0 && mat.get(i - 1, j)[0] == maxValue) {
            erase(mat, i - 1, j, res, number, endi, endj);
        }
        while (i + 1 < mat.rows() && mat.get(i + 1, j)[0] == maxValue) {
            erase(mat, i + 1, j, res, number, endi, endj);
        }
        while (j - 1 >= 0 && mat.get(i, j - 1)[0] == maxValue) {
            erase(mat, i, j - 1, res, number, endi, endj);
        }
        while (j + 1 < mat.cols() && mat.get(i, j + 1)[0] == maxValue) {
            erase(mat, i, j + 1, res, number, endi, endj);
        }
    }

    /**
     * 连通域
     */
    class Connected {
        private double value;   //矩阵值
        private AtomicInteger number; //连接数
        private Rect rect; // 外接矩形坐标

        Connected(double value, AtomicInteger number, Rect rect) {
            this.value = value;
            this.number = number;
            this.rect = rect;
        }

        double getValue() {
            return value;
        }

        void setValue(double value) {
            this.value = value;
        }

        AtomicInteger getNumber() {
            return number;
        }

        void setNumber(AtomicInteger number) {
            this.number = number;
        }

        Rect getRect() {
            return rect;
        }

        void setRect(Rect rect) {
            this.rect = rect;
        }
    }
}
