package util;

import entity.ProjectionEntity;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectionUtil {
    /**
     * 二值图投影
     * @param binary
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @param xAxis
     * @return
     */
    public static int [] binaryProjection(Mat binary, int startX, int endX, int startY, int endY, boolean xAxis){
        int [] result = new int[xAxis?binary.cols():binary.rows()];
        for(int i = startX;i < endX;i++){
            for(int j = startY;j < endY;j++){
                if(binary.get(j,i)[0] == 0xff){
                    if(xAxis){
                        //如果是X轴投影
                        result[i]++;
                    }else{
                        //如果是Y轴投影
                        result[j]++;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 灰度图投影4
     * @param gray
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @param step
     * @param xAxis
     * @param dt
     * @return
     */
    public static List<ProjectionEntity> grayProjection(Mat gray, int startX, int endX, int startY, int endY, int step, boolean xAxis, Double dt){
        int w = endX-startX,h=endY-startY;
        int projectionLen = xAxis?w/step+((w%step == 0)?0:1):h/step+((h%step == 0)?0:1);
        double [] projection = new double[projectionLen];
        if(xAxis){
            for(int i = startX;i < endX;i+=step){
                int index = i/step;
                for(int j = startY;j < endY;j++){
                    projection[index] += gray.get(j,i)[0];
                    //overflow use replicate
                    for(int l = 1;l < step;l++){
                        projection[index] += gray.get(j,Math.min(i+l,w - 1))[0];
                    }
                }
                projection[index] /= h * step;
            }
        }else{
            for(int i = startY;i < endY;i+=step){
                int index = i/step;
                for(int j = startX;j < endX;j++){
                    projection[index] += gray.get(i,j)[0];
                    //overflow use replicate
                    for(int l = 1;l < step;l++){
                        projection[index] += gray.get(Math.min(i+l,h - 1),j)[0];
                    }
                }
                projection[index] /= w * step;
            }
        }
        double [] avgGap = new double[2];
        double [] stdev = new double[2];
        double [] gapTuple = new double[projectionLen - 1];
        List<ProjectionEntity> projectionEntities = new ArrayList<>();
        for(int i = 1;i < gapTuple.length;i++){
            gapTuple[i-1] = projection[i]-projection[i-1];
            avgGap[0] += Math.abs(projection[i]-projection[i-1]);
            avgGap[1] += projection[i]-projection[i-1];
            stdev[0] += Math.pow(projection[i]-projection[i-1],2);
            stdev[1] += stdev[0];
            projectionEntities.add(new ProjectionEntity((2*i-1)*step/2,i-1,i,Math.abs(projection[i]-projection[i-1]),projection[i]-projection[i-1]));
        }
        avgGap[0] /= gapTuple.length;
        avgGap[1] /= gapTuple.length;
        stdev[0] = Math.sqrt(stdev[0]/gapTuple.length - avgGap[0]*avgGap[0]);
        stdev[1] = Math.sqrt(stdev[1]/gapTuple.length - avgGap[1]*avgGap[1]);

        Iterator<ProjectionEntity> iterator = projectionEntities.iterator();
        if(dt == null){
            dt = 1.;
            if(stdev[0] > avgGap[0])
                dt = 0.75;
        }
        while(iterator.hasNext()){
            ProjectionEntity gen = iterator.next();
            if(gen.getNum() <= avgGap[0]*dt){
                iterator.remove();
            }
        }

        return projectionEntities;
    }
}
