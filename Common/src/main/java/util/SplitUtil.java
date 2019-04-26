package util;

import entity.BinaryPointsEntity;
import entity.PointEntity;
import entity.PointLocationEntity;
import entity.PointsCoreEntity;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class SplitUtil {

    /**
     * 根据二值化进行切割
     */
    public static List<PointEntity> split(BinaryPointsEntity bp, int height, double avgH, double avgW, double avgSW){
        List<PointEntity> pls = bp.getPoints();
        PointsCoreEntity pc = bp.getPointsCore();
        boolean isGood = pc.isGood();
        if(isGood){
            return pls;
        }
        List<PointEntity>  result =  xSplit(bp,height,avgH,avgW,avgSW);
        return result;
    }

    /**
     *  x direction split
     */
    public static  List<PointEntity> xSplit(BinaryPointsEntity bp, int height, double avgH, double avgW, double avgSW){

        List<PointEntity> pls = bp.getPoints();
        List<PointEntity> psNew = new ArrayList<>();
        PointEntity pOne = pls.get(0);
        PointLocationEntity plOne = ConvertUtil.convert(pOne.getxLeft(),true);
        int pointsLen = pls.size();
        List<PointLocationEntity> result= new ArrayList<>();
        result.add(plOne);
        int newTempStart = pOne.getxLeft();
        int minY1 = pOne.getyTop(), maxY2 = pOne.getyButton();
        // region split by cycle
        for(int i=0;i<pointsLen;i++){
            PointEntity p =  pls.get(i);
            int leftSpace = 0;
            int currentPoint = p.getxLeft();
            if (i>0){
                PointEntity pl =  pls.get(i-1);
                leftSpace = currentPoint - pl.getxRight()-1;
            }
            int rightPoint = p.getxRight();
            int y1 = p.getyTop();
            int y2 = p.getyButton();
            minY1 = Math.min(y1,minY1);
            maxY2 = Math.max(y2,maxY2);
            if(currentPoint>=rightPoint || y1>=y2){
                continue;
            }
            int newRightPoint = rightPoint;
            int tempStart = newTempStart;
            int rightSecondPoint = 0;
            int rightThreePoint = 0;
            int rightFourPoint = 0,rightThreeW =0,rightTwoSpace =0;
            if(i+1<pointsLen){
                PointEntity p2 =  pls.get(i+1);
                rightSecondPoint = p2.getxLeft();
                rightThreePoint = p2.getxRight();
            }
            if(i+2<pointsLen){
                PointEntity p3 =  pls.get(i+2);
                rightFourPoint = p3.getxLeft();
                rightTwoSpace = rightFourPoint - rightThreePoint-1;
                rightThreeW = p3.getWidth();
            }
            boolean  isLast = i < pointsLen - 1;
            int rightSpace = rightSecondPoint-rightPoint-1;
            int srDiff = rightThreePoint - tempStart;
            int diff = rightPoint - tempStart+1;
            double numRadio =( diff+avgSW)/(avgW + avgSW);
            boolean blright = srDiff>avgW - avgSW && srDiff<avgSW+avgW;
            boolean blone = diff>avgW - 1.158*avgSW && diff<=avgW+1.38*avgSW;
            boolean blTwo =  diff<=avgW - 1.158*avgSW && diff>avgW-1.439*avgSW && (diff>=5 || avgW-diff<4) && (rightSpace>0.968*avgSW || !blright);
            boolean blThreeOne = !blright && (rightSpace>1.08*avgSW  && diff+rightSpace>0.918*avgW || (rightSpace>0.868*avgSW && diff+rightSpace>0.808*avgW && srDiff>avgW+1.208*avgSW)) ;
            boolean blThree =   diff<=avgW - 1.258*avgSW && diff>avgW-1.758*avgSW && ((rightSpace>2*avgSW && diff+rightSpace>avgW+0.528*avgSW) || blThreeOne);
            boolean blFour = diff<=avgW - 1.258*avgSW &&  diff>avgW-1.768*avgSW && diff+rightSpace+leftSpace>0.928*(avgSW+avgW) && rightSpace>1.168*avgSW;
            boolean blFive = diff<=avgW - 1.398*avgSW &&  diff>avgW-1.838*avgSW  && srDiff>(avgSW+avgW) && rightSpace>1.068*avgSW && avgSW<3.18;
            boolean blSix = diff<=avgW - 1.558*avgSW &&  diff>avgW-1.688*avgSW && !blright && diff+rightSpace+leftSpace>0.888*(avgSW+avgW) && rightSpace>0.798*avgSW && srDiff>1.556*avgSW;
            boolean blSeven = !blright && diff>0.398*avgW && diff<avgW-avgSW  && diff+rightSpace+leftSpace>0.798*(avgW+avgSW);
            // 签发机关 --cn
            boolean blEight = !blright && diff>0.508*avgW && diff<avgW-avgSW &&  diff+rightSpace>0.708*(avgW+avgSW) && srDiff>1.528*(avgW+avgSW);
            boolean isGoodP = isGoodPoint(p);
            if( (blone || blTwo || blThree || blFour || blFive || blSix || blSeven || blEight) && isLast && isGoodP){
                boolean tempOne = rightSpace<avgSW && Math.abs(srDiff-avgW)+1<=Math.abs(diff-avgW);
                boolean tempTwo = (rightSpace<=0 || (rightSpace<0.298*avgSW && rightSpace<3)) &&  Math.abs(srDiff-avgW)<1.608*Math.abs(diff-avgW);
                boolean blrightOne = rightSpace<0 && rightSpace<0.198*avgSW && srDiff>avgW - avgSW &&  srDiff<1.198*avgSW+avgW && diff<1.08*avgW;
                // name -- cn
                boolean blrightTwo = diff<1.08*avgW && (srDiff<1.298*avgW || (srDiff<1.398*avgW &&  rightThreeW+(srDiff-diff)> srDiff+1.58*avgSW)) && srDiff<1.298*avgH && rightThreeW+(srDiff-diff)>srDiff+avgSW && rightTwoSpace>0 ;
                boolean blGoodRight = (blright && (tempOne || tempTwo)) || blrightOne || blrightTwo;
                if(blGoodRight){
                    continue;
                }
                if(blThree && diff<avgW-avgSW && diff<1.28*avgSW ){
                    newRightPoint = rightPoint+(int)Math.round(avgSW);
                }
                PointEntity pn = ConvertUtil.convertPoint(newTempStart,newRightPoint,minY1,maxY2);
                psNew.add(pn);
                newTempStart = rightSecondPoint;
            }else if(diff>avgW+1.38*avgSW){
                int tempWordNum = (int)Math.round(numRadio);
                PointEntity pTemp =  new PointEntity();
                pTemp.setxLeft(tempStart);
                pTemp.setyTop(y1);
                pTemp.setyButton(y2);
                if(numRadio>1.808 || (numRadio>1.598 && diff>1.928*avgW && avgW>2.568*avgSW)){
                    double tempAvg = diff/(double) tempWordNum;
                    for (int j = 0; j < tempWordNum - 1; j++)
                    {
                        Mat img = bp.getBwImage();
                        int w = img.width();
                        int start = pTemp.getxLeft();
                        double offSet = Math.max(0.158*avgW,Math.min(0.278*avgW,0.8*avgSW));
                        int right =(int) (start + tempAvg -offSet);
                        right = Math.max(0,right);
                        int left = (int) (right + 2*offSet);
                        left = Math.min(w-1,left);
                        if(right>w-0.598*avgW || left>w+0.2*avgW){
                            break;
                        }
                        PointEntity ps = findBestSeparation( bp, right, left, y1, y2, height, offSet, avgW, avgSW, start);
                        right = ps.getxLeft();
                        left = ps.getxRight();
                        pTemp = ConvertUtil.convertPoint(start,right,y1,y2);
                        if(isGoodPoint(pTemp)){
                            psNew.add(pTemp);
                        }
                        pTemp = new PointEntity();
                        pTemp.setyTop(y1);
                        pTemp.setyButton(y2);
                        pTemp.setxLeft(left);
                    }
                    pTemp = ConvertUtil.convertPoint(pTemp.getxLeft(),rightPoint,y1,y2);
                    if(isGoodPoint(pTemp)){
                        psNew.add(pTemp);
                    }
                }else{
                    pTemp = ConvertUtil.convertPoint(pTemp.getxLeft(),rightPoint,y1,y2);
                    if(isGoodPoint(pTemp)){
                        psNew.add(pTemp);
                    }
                }
                newTempStart = rightSecondPoint;
            }else if(i==pointsLen-1 && isGoodP){
                if(p.getxLeft()>newTempStart){
                    PointEntity pn = ConvertUtil.convertPoint(newTempStart,newRightPoint,minY1,maxY2);
                    psNew.add(pn);
                }
                else {
                    if(p.getWidth()>0.328*avgW && p.getHeight()>0.258*avgH){
                        psNew.add(p);
                    }
                }
            }
        }
        // endregion
        return psNew;
    }


    // find best cut point
    public static PointEntity findBestSeparation(BinaryPointsEntity bp, int right, int left, int y1, int y2, int height, double offSet, double avgW, double avgSW, int start){

        PointEntity p = new PointEntity();
        Mat img = bp.getImage();
        Mat bw = bp.getBwImage();
        int[] bwProjection = SeparationUtil.binaryProjection(bw, right, left, y1, y2, true,0xff);
        int[] minResult = SeparationUtil.findMinSepatation( bwProjection, right, left,height);
        int maxStep = Math.max(1,(int) Math.round(0.5*offSet));
        int xm = right+(left-right)/2;
        boolean isGood = false;
        int minIndex = 0, minValue = 0;
        if(minResult.length==2 && minResult[1]<0.439*height){
            minIndex = minResult[0];  minValue = minResult[1];
            int leftMaxStep = Math.min(maxStep,minIndex-right);
            int newRight = findLeftMin(  bwProjection , minIndex-1,minValue, leftMaxStep);
            int rightMaxStep = Math.min(left-minIndex,maxStep);
            int newLeft = findRightMin(  bwProjection ,minIndex+1,minValue, rightMaxStep);
            isGood = isGood(newRight, xm, offSet,newLeft, avgW);
            if(isGood){
                right = newRight;
                left  = newLeft;
            }else {
                int[] bwProjectionY = SeparationUtil.binaryProjection(bw, minIndex, minIndex+1, y1, y2, false,0xff);
                int[] startAndEnd = SeparationUtil.findStartAndEnd(bwProjectionY,  y1, y2,0);
                int yStart = startAndEnd[0], yEnd = startAndEnd[1];
                if(yEnd-yStart>1.28*minValue ){
                    int newMinIndex = findMinSumIndex( bwProjection , bw, right,left, y1, y2, minValue);
                    newRight = newMinIndex-1;
                    newLeft = newMinIndex+1;
                    isGood = isGood(newRight, xm, offSet,newLeft, avgW);
                    if(isGood){
                        right = newRight;
                        left  = newLeft;
                    }
                }
            }
        }
        if(!isGood){
            Mat sub = img.submat(y1,y2,right,left+1);
            int subH = sub.height(), subW = sub.width();
            Mat subBw = BinaryUtil.getBinaryImg(sub,0.598);
            bwProjection = SeparationUtil.binaryProjection(subBw, 0, subW, 0, subH, true,0xff);
            minResult = SeparationUtil.findMinSepatation( bwProjection, 0, subW,height);
            if(minResult.length==2 && minResult[1]<0.439*height){
                int leftMaxStep = Math.min(maxStep,minResult[0]);
                int rightOffset = findLeftMin(  bwProjection , minResult[0]-1,minResult[1], leftMaxStep);
                int rightMaxStep = Math.min(subW-minResult[0],maxStep);
                int leftOffset = findRightMin(  bwProjection ,minResult[0]+1,minResult[1], rightMaxStep);
                left = right+leftOffset;
                right = right +rightOffset;
                isGood = true;
            }
        }
        if(!isGood){
            right =  (int) (start + avgW +1);
            left =  (int) (right + avgSW -1);
        }
        p.setxLeft(right);
        p.setxRight(left);
        return p;
    }

    // find min sum index
    public static int findMinSumIndex(int[] bwProjection , Mat bw, int startIndex, int endIndex, int y1, int y2, int minValue){
        int minIndex = -1;
        int minSum =0;
        int goodCount =0;
        for(int i = startIndex;i<=endIndex;i++){
            int value = bwProjection[i];
            int h = y2-y1;
            if(value<1.5*minValue){
                int[] bwProjectionY = SeparationUtil.binaryProjection(bw, i, i+1, y1, y2, false,0xff);
                int[] startAndEnd = SeparationUtil.findStartAndEnd(bwProjectionY,  y1, y2,0);
                int yStart = y1, yEnd = y2;
                if(startAndEnd!=null && startAndEnd.length==2){
                    yStart = startAndEnd[0];
                    yEnd = startAndEnd[1];
                }
                int nh = yEnd - yStart+1;
                if(nh<1.368*value && nh<0.528*h){
                    int sum = nh+value;
                    if(goodCount==0){
                        minIndex = i;
                        minSum = sum;
                    }else {
                        if(sum<minSum){
                            minSum = sum;
                            minIndex = i;
                        }
                    }
                    goodCount ++;
                }
            }
        }
        return minIndex;
    }


    // is good separation
    private static boolean isGood(int right,int xm,double offSet,int left,double avgW){

        boolean isGood = Math.abs(right-xm)<0.768*offSet && Math.abs(left-xm)<0.768*offSet && Math.abs(right-xm)+ Math.abs(left-xm)<1.238*offSet &&  Math.abs(right-xm)<0.238*avgW && Math.abs(left-xm)<0.238*avgW &&  Math.abs(right-xm)+ Math.abs(left-xm)<0.368*avgW;
        return isGood;

    }

    // find left first big index
    private static  int findLeftMin( int[] bwProjection ,int index,int minValue,int maxStep){
        int result = index;
        int len = bwProjection.length;
        if(index>=0 && index<len){
            for(int i = index;i>=0;i--){
                if(bwProjection[i]>minValue){
                    result = i;
                    break;
                }
                if(index-i>=maxStep){
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    // find right first big index
    private static int findRightMin( int[] bwProjection ,int index,int minValue,int maxStep){
        int result = index;
        int len = bwProjection.length;
        if(index>=0 && index<len){
            for(int i = index;i<len;i++){
                if(bwProjection[i]>minValue){
                    result = i;
                    break;
                }
                if(i-index>=maxStep){
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean isGoodPoint(PointEntity p){
        boolean result = false;
        if(p!=null && p.getxRight()>p.getxLeft() && p.getyButton()>p.getyTop()){
            result = true;
        }
        return result;
    }



}
