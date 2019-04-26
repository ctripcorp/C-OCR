package util;

import entity.PointEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by denghuang on 2018/8/2.
 */
public class SortUtil {

    public static List<PointEntity> reorder(List<PointEntity> lis, boolean isX, boolean isY ){

        if(lis!=null && lis.size()>0){
            // 按x方向排序
            if(isX){
                Collections.sort(lis, new Comparator<PointEntity>() {
                    public int compare(PointEntity arg0, PointEntity arg1) {
                        int hits0 = arg0.getxLeft();
                        int hits1 = arg1.getxLeft();
                        return compareResult(hits1,hits0);
                    }
                });

            }
            // 按y方向排序
            if(isY){
                Collections.sort(lis, new Comparator<PointEntity>() {
                    public int compare(PointEntity arg0, PointEntity arg1) {
                        int hits0 = arg0.getyTop();
                        int hits1 = arg1.getyTop();
                        return compareResult(hits1,hits0);
                    }
                });
            }

        }

        return lis;
    }

    private static int compareResult(int hits1,int hist0){
        if (hits1 > hist0) {
            return -1;
        } else if (hits1 == hist0) {
            return 0;
        } else {
            return 1;
        }
    }
}
