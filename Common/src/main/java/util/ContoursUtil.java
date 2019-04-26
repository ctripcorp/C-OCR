package util;


import entity.BinaryPointsEntity;
import entity.PointEntity;
import entity.PointsCoreEntity;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ContoursUtil {

    // find binary image bounding rect
    // mode :Imgproc.RETR_EXTERNAL method: Imgproc.CHAIN_APPROX_SIMPLE  minArea 4
    public static BinaryPointsEntity getBinaryBaseInfoByFindContours(Mat src, int mode, int method, Point offset, double whMaxRadio, double whMinRadio, int minArea){

        BinaryPointsEntity bp = new BinaryPointsEntity();
        int height = src.height();
        int width = src.width();
        int max = Math.max(height,width);
        int min = Math.min(height,width);
        PointsCoreEntity pc = new PointsCoreEntity();
        List<PointEntity> points = new ArrayList<>();
        List<MatOfPoint> contours =  getCounters(src, mode, method, offset);
        int maxArea = 0;
        if(contours!=null && contours.size()>0){
            int pointsLen = 0;
            int xLeftMin = width,xRightMax = 0, maxWidth = 0,minWidth = width;
            int yTopMin =  height,yTopMax = 0,yButtonMin = height,yButtonMax = 0,maxHeight = 0,minHeight = height;
            int goodWordCount = 0,goodYTopMin = height,goodYButtonMax =0;
            double goodSumWidth=0, goodSumHeight = 0,goodYTopSum=0,goodYButtonSum=0;
            double hAvg =0,wAvg =0;
            double sumWidth = 0,sumHeight = 0;
            double yTopSum = 0,yButtonSum = 0;
            double spaceSum = 0,badSpaceSum =0;
            int spaceCount = 0,badSpaceCount = 0;
            int minSpace = width,maxSpace = 0;
            for (int i = 0; i < contours.size(); i++)
            {
                MatOfPoint point = contours.get(i);
                Rect r = Imgproc.boundingRect(point);
                int x1 = r.x, y1 = r.y,w = r.width,h = r.height;
                int x2 = x1 + w -1;
                int y2 = y1 + h -1;
                double radio = (double)(w)/(h);
                if(w*h<=minArea){
                    continue;
                }
                int area = w*h;
                maxArea = Math.max(area,maxArea);
                pointsLen++;
                PointEntity p = new PointEntity(x1,y1,x2,y2,w,h,radio);
                xLeftMin = Math.min(xLeftMin,x1);
                xRightMax = Math.max(xRightMax,x2);
                yTopMin = Math.min(yTopMin,y1);
                yTopMax = Math.max(yTopMax,y1);
                yButtonMax = Math.max(yButtonMax,y2);
                yButtonMin = Math.min(yButtonMin,y2);
                maxWidth = Math.max(maxWidth,w);
                minWidth = Math.min(minWidth,w);
                maxHeight = Math.max(maxHeight,h);
                minHeight = Math.min(minHeight,h);
                boolean isGood =  (h<0.628*max && (h>0.2*min || h>12)  && goodWordCount<=3 )|| (goodWordCount>3 && h>0.469*hAvg && (h<2.98*hAvg || (h<3.98*hAvg && h<16) ));
                boolean isGoodWH = area>10 || h>0.808*min;
                if(radio<whMaxRadio && radio>whMinRadio && isGood && isGoodWH){
                    goodYTopMin = Math.min(goodYTopMin,y1);
                    goodYButtonMax = Math.max(goodYButtonMax,y2);
                    goodSumWidth += w;
                    goodSumHeight +=h;
                    goodYTopSum +=y1;
                    goodYButtonSum +=y2;
                    goodWordCount++;
                    hAvg = goodSumHeight/goodWordCount;
                }
                sumHeight += h;
                sumWidth += w;
                yTopSum += y1;
                yButtonSum += y2;
                points.add(p);
            }
            hAvg = sumHeight/pointsLen;
            wAvg = sumWidth/pointsLen;
            double yTopAvg = yTopSum/pointsLen;
            double yButtonAvg = yButtonSum/pointsLen;
            if(goodWordCount>2){
                hAvg = goodSumHeight/goodWordCount;
                wAvg = goodSumWidth/goodWordCount;
                yTopAvg = goodYTopSum/goodWordCount;
                yButtonAvg = goodYButtonSum/goodWordCount;
            }
            else if( pointsLen>5 ){
                hAvg = (sumHeight-minHeight-maxHeight)/(pointsLen-2);
                wAvg =  (sumWidth-minWidth-maxWidth)/(pointsLen-2);
                yTopAvg = (yTopSum-yTopMin-yTopMax)/(pointsLen-2);
                yButtonAvg = (yButtonSum-yButtonMax - yButtonMin)/(pointsLen-2);
            }
            double spaceAvg = 0;

            pc = new PointsCoreEntity(pointsLen,spaceCount, maxWidth, minWidth,wAvg, spaceAvg,maxSpace,minSpace,badSpaceCount,badSpaceSum, xLeftMin, xRightMax, hAvg,
                    yTopAvg, yButtonAvg, yTopMin, yButtonMax, goodWordCount, sumWidth, sumHeight, minHeight, maxHeight,goodYTopMin,goodYButtonMax,false);
            pc.setMaxArea(maxArea);

        }
        bp.setPointsCore(pc);
        bp.setPoints(points);
        bp.setBwImage(src);


        return bp;
    }


    /*
     mode RETR_EXTERNAL：表示只提取最外面的轮廓  RETR_LIST：表示提取所有轮廓并将其放入列表
     method CHAIN_APPROX_SIMPLE：压缩水平、垂直和对角直线段，仅保留它们的端点；
     offset 偏移
     */
    public static  List<MatOfPoint> getCounters(Mat src, int mode, int method, Point offset){
        List<MatOfPoint> contours = null;
        if(src!=null){
            Mat dst = src.clone();
            if(dst.channels() != 1){
                Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2GRAY);
            }
            contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(dst, contours, hierarchy, mode, method, offset);
        }
        return contours;
    }

}
