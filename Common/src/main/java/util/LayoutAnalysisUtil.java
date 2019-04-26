package util;

import entity.BinaryPointsEntity;
import org.opencv.core.Mat;
import org.opencv.core.Point;


public class LayoutAnalysisUtil {

    // get binary info
    public static BinaryPointsEntity getBinaryInfo(Mat sub, double whMaxRadio, double whMinRadio, int dtX, boolean isNo,int minArea){

        BinaryPointsEntity bp = null;
        if(sub!=null && sub.height()>0){
            bp = SeparationUtil.getBoundingPoints(sub,new Point(0,0),whMaxRadio, whMinRadio,dtX,false,isNo, minArea);
            bp.setImage(sub);
        }
        return bp;
    }



}
