package separation;

import entity.BinaryPointsEntity;
import entity.PointEntity;
import entity.PointsCoreEntity;
import org.opencv.core.Mat;
import util.LayoutAnalysisUtil;
import util.SplitUtil;

import java.util.List;

public class SeparationBiz {


    /**
     * 获取拒识结果
     *
     * @param img 原图
     * @param isNo true表示是数字或字母 false 表示是汉字
     * @param whMaxRadio 宽高比最大值
     * @param whMinRadio 宽高比最小值
     * @param dtX 阈值
     * @param minArea 最小过滤面积值
     * @return 切割结果
     */
     public static List<PointEntity> separaion(Mat img,boolean isNo,double whMaxRadio,double whMinRadio,int dtX,int minArea){

         BinaryPointsEntity bp = LayoutAnalysisUtil.getBinaryInfo(img, whMaxRadio,  whMinRadio,  dtX,  isNo, minArea);
         List<PointEntity> points = getCutPoints( bp);
         return points;

     }

     private static List<PointEntity> getCutPoints(BinaryPointsEntity bp){

        List<PointEntity> pls = null;
        if(bp!=null && bp.getPoints()!=null && bp.getPoints().size()>1 ){
            pls = bp.getPoints();
            PointsCoreEntity pc = bp.getPointsCore();
            if(pc!=null){
                int yStart = pc.getyStart();
                int yEnd = pc.getyEnd();
                int tieHeight = yEnd - yStart+1;
                pls  =  SplitUtil.split( bp, tieHeight, pc.getAvgHeight(), pc.getAvgDiff(), pc.getAvgSpaceDiff() );
            }
        }
        return pls;
    }

}
