package verificationcode;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.*;

/**
 * Created by jiangpeng on 2018/11/19.
 */
public class RectUtil {

    private RectUtil() {
        throw new IllegalStateException("PointsUtil class");
    }


    /**
     * 过滤内部的轮廓点，最外层的轮廓0,0
     */
    public static List<Rect> filterInnerRect(Mat mat, List<Rect> contourList) {
        List<Rect> contourFilter = new LinkedList<>();
        for (int i = 0; i < contourList.size(); i++) {
            Rect rect = contourList.get(i);
            boolean outArea = true;
            if (rect.x == 0 && rect.y == 0 && rect.width >= mat.width() && rect.height >= mat.height()) {
                continue;
            }
            for (int j = 0; j < contourList.size(); j++) {
                Rect rect2 = contourList.get(j);
                if (rect2.x == 0 && rect2.y == 0 && rect2.width >= mat.width() && rect2.height >= mat.height()) {
                    continue;
                }
                if (!rect.equals(rect2) && rect.x >= rect2.x && rect.y >= rect2.y && (rect.x + rect.width) <= (rect2.x + rect2.width) && (rect.y + rect.height) <= (rect2.y + rect2.height)) {
                    outArea = false;
                    break;
                }
            }
            if (outArea) {
                contourFilter.add(rect);
            }
        }
        return contourFilter;
    }

    /**
     * 合并相交矩形
     *
     * @param points
     * @return
     */
    public static List<Rect> mergeRects(List<Rect> points) {
        for (int io = 0; io < points.size(); io++) {
            Rect recto = points.get(io);
            if (recto != null) {
                for (int ii = 0; ii < points.size(); ii++) {
                    Rect recti = points.get(ii);
                    if (io != ii && recti != null) {
                        int[] itemo = rect2Points(recto);
                        int[] itemi = rect2Points(recti);
                        int zx = Math.abs(itemi[0] + itemi[2] - itemo[0] - itemo[2]);
                        int x = Math.abs(itemi[0] - itemi[2]) + Math.abs(itemo[0] - itemo[2]);
                        int zy = Math.abs(itemi[1] + itemi[3] - itemo[1] - itemo[3]);
                        int y = Math.abs(itemi[1] - itemi[3]) + Math.abs(itemo[1] - itemo[3]);
                        if (zx <= x && zy <= y) {
                            //  两个矩形相交
                            recto = mergeRect(recto, recti);
                            points.set(io, recto);
                            points.set(ii, null);
                            io = 0;     //恢复，重头开始遍历
                            break;
                        }
                    }
                }
            }
        }
        points.removeIf((Rect rect) -> rect == null);
        return points;
    }

    public static Rect mergeRect(Rect rect1, Rect rect2) {
        if (rect1 == null || rect2 == null) {
            return null;
        }
        Rect rect = new Rect();
        rect.x = Math.min(rect1.x, rect2.x);
        rect.y = Math.min(rect1.y, rect2.y);
        rect.width = Math.max(rect1.x + rect1.width, rect2.x + rect2.width) - rect.x;
        rect.height = Math.max(rect1.y + rect1.height, rect2.y + rect2.height) - rect.y;
        return rect;
    }

    public static List<Rect> mergeRowRects(List<Rect> rowRects, int space) {
        if (rowRects == null || rowRects.isEmpty()) {
            return Collections.emptyList();
        }
        Comparator<Rect> sortByX = Comparator.comparing(u -> u.x);
        rowRects.sort(sortByX);
        List<Rect> rects = new ArrayList<>();
        rects.add(rowRects.get(0));
        for (int i = 1; i < rowRects.size(); i++) {
            Rect last = rects.get(rects.size() - 1);
            Rect temp = rowRects.get(i);
            if (temp != null && temp.x - (last.x + last.width) < space) {
                last.width = Math.max(temp.x + temp.width, last.x + last.width) - last.x;
                last.height = Math.max(last.y + last.height, temp.y + temp.height) - Math.min(last.y, temp.y);
                last.y = Math.min(last.y, temp.y);
            } else {
                rects.add(temp);
            }
        }
        return rects;
    }

    public static int[] rect2Points(Rect rect) {
        return new int[]{rect.x, rect.y, rect.x + rect.width, rect.y + rect.height};
    }


    public static List<Rect> points2Rects(List<int[]> points) {
        if (points == null || points.isEmpty()) {
            return Collections.emptyList();
        }
        List<Rect> cutPoints = new ArrayList<>();
        for (int[] ints : points) {
            Rect rect = new Rect();
            rect.x = ints[0];
            rect.y = ints[1];
            rect.width = ints[2] - ints[0];
            rect.height = ints[3] - ints[1];
            cutPoints.add(rect);
        }
        return cutPoints;
    }

    public static List<Rect> deepCopy(List<Rect> src) {
        if (src == null || src.isEmpty()) {
            return Collections.emptyList();
        }
        List<Rect> dst = new LinkedList<>();
        for (Rect rect : src) {
            dst.add(rect.clone());
        }
        return dst;
    }

    /**
     * 坐标补偿
     *
     * @param rect
     * @param top
     * @param right
     * @param bottom
     * @param left
     * @return
     */
    public static Rect rectOffset(Rect rect, int top, int right, int bottom, int left) {
        if (rect == null) {
            return null;
        }
        Rect result = new Rect();
        result.x = rect.x - left > 0 ? rect.x - left : 0;
        result.y = rect.y - top > 0 ? rect.y - top : 0;
        result.width = rect.width + left + right;
        result.height = rect.height + top + bottom;
        return result;
    }

    /**
     * 相对于外层的坐标矫正
     *
     * @param rect
     * @param parentRect
     * @return
     */
    public static void correctForParentRect(Rect rect, Rect parentRect) {
        if (rect == null || parentRect == null) {
            return;
        }
        rect.x = rect.x + parentRect.x;
        rect.y = rect.y + parentRect.y;
    }

    public static void correctForParentRect(List<Rect> rects, Rect parentRect) {
        if (rects == null || rects.isEmpty() || parentRect == null) {
            return;
        }
        for (Rect rect : rects) {
            correctForParentRect(rect, parentRect);
        }
    }

    //白边引起的坐标偏移矫正
    public static void whiteEdgeCorrect(List<Rect> rects) {
        if (rects == null || rects.isEmpty()) {
            return;
        }
        for (Rect rect : rects) {
            if (rect.x == 0) {
                rect.width = rect.width - 2;
            } else {
                rect.x = rect.x - 1;
                rect.width = rect.width - 1;
            }
            if (rect.y == 0) {
                rect.height = rect.height - 2;
            } else {
                rect.y = rect.y - 1;
                rect.height = rect.height - 1;
            }
        }
    }

}
