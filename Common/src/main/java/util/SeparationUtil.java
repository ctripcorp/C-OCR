package util;

import entity.*;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SeparationUtil {

    // find min result
    public static int[] findMinSepatation(int[] bwProjection, int start, int end,int height)
    {
        int[] result = new int[] { end, height };

        if (bwProjection != null && bwProjection.length > 3)
        {
            int min = end;
            int minTwo = 4 * height;
            int tempMin = height;
            int minIndex = start;
            int len = bwProjection.length;
            if (end >= len)
            {
                end = len - 1;
            }
            for (int i = start; i <= end; i++)
            {
                if (i <= end - 3 && end - 3 >= 0)
                {
                    tempMin = bwProjection[i] + bwProjection[i + 1] + bwProjection[i + 2] + bwProjection[i + 3];
                }
                if (tempMin < minTwo && i < end - 4)
                {
                    minTwo = tempMin;
                    min = bwProjection[i];
                    minIndex = i;
                    for (int j = 1; j < 4; j++)
                    {
                        if (bwProjection[i + j] <= min)
                        {
                            min = bwProjection[i + j];
                            minIndex = i + j;
                        }
                    }

                }
            }

            result[0] = minIndex;
            result[1] = min;
        }

        return result;
    }

    public static int[] findStartAndEnd(int[] bwProjection, int start, int end,int dt){
        int[] result = new int[] { start, end };
        int starttIndex = start, endIndex = end;
        boolean isStart = true, isEnd = true;
        int j = end;
        if (bwProjection != null && bwProjection.length > 1){
            for (int i = start; i <= end; i++)
            {
                if(isStart && bwProjection[i]>dt){
                    starttIndex = i;
                    isStart = false;
                }
                if(isEnd && bwProjection[j]>dt){
                    endIndex = j;
                    isEnd = false;
                }
                if(!isStart && !isEnd){
                    break;
                }
                j --;
            }
            result[0] = starttIndex;
            result[1] = endIndex;
        }
        return result;
    }

    // 根据二值化分隔
    public static SeparationPointEntity separaionByBinary(BinaryProjectionEntity projection, int start, int end, int dt, double stdAvg, int maxXorY, String type, boolean isXProjection){

        SeparationPointEntity sp = new SeparationPointEntity();

        List<PointLocationEntity> list = new ArrayList<>();
        List<Integer> listDiff = new ArrayList<>();
        PointLocationEntity pointLocation = new PointLocationEntity();
        int[] bwProjection;
        if(isXProjection){
            bwProjection = projection.bwProjectionX;
        } else {
            bwProjection = projection.bwProjectionY;
        }

        pointLocation.isLeft = true;
        pointLocation.pointLocation = start;
        list.add(pointLocation);
        int diff = 0;
        int brightSum = 0;
        double whiteLen = 0;
        double blackLen = 0;
        int whiteCout = 0;
        int blackCout = 0;
        int bwLen = bwProjection.length;
        if (bwLen == end)
        {
            end = end - 1;
        }
        int maxDiff = 0;
        int minDiff = end - start + 1;
        int maxDiffEndIndex = 0;
        int minDiffEndIndex = end;
        int maxBalckDiff = 0;
        int maxBlackDiffIndex = 0;
        int minBalckDiff =end - start + 1;
        int minBlackDiffIndex = 0;

        double whiteSum = 0;

        int k=0;
        for (int i = start + 1; i <= end; i++)
        {
            brightSum = brightSum + bwProjection[i];


            boolean right = false;
            boolean left = false;

            if (i + 1 <= end && end < bwLen)
            {
                left = (bwProjection[i] <= 2 && bwProjection[i + 1] > 2 && i >= dt + start + 1 && i < end - dt - 1);
                right = (bwProjection[i + 1] <= 2 && bwProjection[i] > 2 && i >= dt + start + 1 && i < end - dt - 1);
            }
            else if (i == end)
            {
                right = bwProjection[end] > 0 && bwProjection[end - 1] > 0;
            }
            int listCount = list.size();
            if ( listCount>=1 && (left && !list.get(listCount-1).isLeft) || (right && list.get(listCount-1).isLeft))
            {
                k = k + 1;
                pointLocation = new PointLocationEntity();

                if (left)
                {
                    pointLocation.pointLocation = i + 1;

                }
                else
                {
                    pointLocation.pointLocation = i;
                }

                pointLocation.isLeft = left;
                list.add(pointLocation);

                if ( k > 0 && list.size() > 1 && k < list.size())
                {
                    diff = list.get(k).pointLocation -  list.get(k-1).pointLocation + 1;
                    if (list.get(k).isLeft && k > 1)
                    {
                        diff = diff - 2;
                        blackLen = blackLen + diff;
                        blackCout += 1;
                        if ( diff > maxBalckDiff)
                        {
                            maxBalckDiff = diff;
                            maxBlackDiffIndex = k;
                        }
                        if(diff<minBalckDiff){
                            minBalckDiff = diff;
                            minBlackDiffIndex = k;
                        }
                    }else if(!list.get(k).isLeft){
                        whiteLen = whiteLen + diff;
                        whiteCout += 1;
                        whiteSum = whiteSum + diff;
                        listDiff.add(diff);
                        if (diff >= maxDiff )
                        {
                            maxDiff = diff;
                            maxDiffEndIndex = k;
                        }
                        if (diff < minDiff)
                        {
                            minDiff = diff;
                            minDiffEndIndex = k;
                        }
                    }
                }

            }
        }

        sp.binaryProjection = projection;
        sp.pointLocations = list;
        sp.listDiff = listDiff;
        sp.brightSum = brightSum;
        sp.whiteCount = whiteCout;
        sp.whiteLen = whiteLen;
        sp.blackCount  = blackCout;
        sp.blackLen = blackLen;
        sp.maxDiff = maxDiff;
        sp.maxDiffEndIndex = maxDiffEndIndex;
        sp.minDiff = minDiff;
        sp.minDiffEndIndex = minDiffEndIndex;
        sp.blackMaxDiff = maxBalckDiff;
        sp.blackMaxDiffEndIndex = maxBlackDiffIndex;
        sp.blackMinDiff = minBalckDiff;
        sp.blackMinDiffEndIndex = minBlackDiffIndex;

        return sp;

    }


    // 获取x和也方向投影并绑紧图像
    public static BinaryProjectionEntity getProjection(Mat origin,int dtX,int dtY){
        BinaryProjectionEntity binaryProjection = new BinaryProjectionEntity();

        int width = origin.width();
        int height = origin.height();
        double radio = (double) width/(height);
        Mat bw = BinaryUtil.otsuNew(origin);
//        if(radio<8.568){
//            bw = otsu(origin);
//        } else{
//            bw = sauvola(origin);
//        }
        // 中值滤波
        Imgproc.medianBlur(bw,bw,3);

        int xStart = 0;
        int xEnd = width - 1;
        int yStart = 0;
        int yEnd = height - 1;
        int[] bwProjectionY = binaryProjection(bw, 0, width, 0, height, false,0xff);
        int[] bwProjectionX = binaryProjection(bw, 0, width, 0, height, true,0xff);
        // 初步求出上下左右四个分隔点
        int[] preCutY = preSeparation(bwProjectionY, yStart, yEnd, dtY);
        int[] preCutX = preSeparation(bwProjectionX, xStart, xEnd, dtX);
        if (preCutX != null && preCutX.length == 2)
        {
            xStart = preCutX[0];
            xEnd = preCutX[1];
        }
        if (preCutY != null && preCutY.length == 2)
        {
            yStart = preCutY[0];
            yEnd = preCutY[1];
        }

        int newWidth = xEnd - xStart + 1;
        int newHeight = yEnd - yStart + 1;

        PointResultEntity pointResult = new PointResultEntity();
        pointResult.xStart = xStart;
        pointResult.xEnd = xEnd;
        pointResult.yStart = yStart;
        pointResult.yEnd = yEnd;
        pointResult.width = newWidth;
        pointResult.height = newHeight;

        binaryProjection.pointResult = pointResult;
        binaryProjection.bwProjectionX = bwProjectionX;
        binaryProjection.bwProjectionY = bwProjectionY;
        binaryProjection.bwImage = bw;

        return binaryProjection;

    }

    // 求上下左右四个点
    public static int[] preSeparation(int[] bwProjection, int start, int end, int dt)
    {
        int[] result = new int[] { start, end };
        int len = end - start + 1;
        boolean fistFlag = true;
        boolean lastFlag = true;
        if (len >= 6)
        {
            int firstSum = bwProjection[start];
            int secondSum = bwProjection[start+1];
            int threeSum = bwProjection[start+2];
            int fourSum = 0;
            int fiveSum = 0;

            int lastfirstSum = bwProjection[end];
            int lastsecondSum = bwProjection[end-1];
            int lastthreeSum = bwProjection[end-2];
            int lastfourSum = 0;
            int lastfiveSum = 0;
            int th = dt - 1;
            if (th < 0)
            {
                th = 0;
            }

            boolean blOne = firstSum > dt && secondSum >= dt && threeSum >= th && (secondSum>0 || firstSum>=5);

            boolean blTwo = lastfirstSum > dt && lastsecondSum >= dt && lastthreeSum >= th && (lastsecondSum>0 && lastfirstSum>=5);

            int k = 0;
            for (int i = start; i <=end; i++)
            {
                if (bwProjection[i] > 0 && fistFlag)
                {
                    if (blOne && i<=2)
                    {
                        result[0] = 0;
                        fistFlag = false;
                    }
                    else if(i+4<len-1)
                    {
                        firstSum = bwProjection[i];
                        secondSum = bwProjection[i + 1];
                        threeSum = bwProjection[i + 2];
                        fourSum = bwProjection[i + 3];
                        fiveSum = bwProjection[i + 4];
                        boolean bone = firstSum >= dt && secondSum >= 1 && threeSum >= 1 && fourSum >= 1 && fiveSum >= 1;
                        boolean btwo = firstSum >= dt + 6 && secondSum >= dt + 6;
                        boolean bthree = firstSum >= dt && secondSum >= dt && threeSum >=dt && secondSum>0;
                        boolean bfour = firstSum > dt && secondSum >= dt && threeSum >= th && secondSum>0;
                        boolean bfive = firstSum > dt && secondSum>=dt && i>5;
                        boolean bsix = i > len / 3 && bwProjection[i] > 0 && i+1<end && bwProjection[i+1] > 0;
                        boolean bseven = Math.min(firstSum,secondSum)>0 && Math.max(firstSum,secondSum)>9 && Math.max(firstSum,secondSum)>6*dt;
                        if (bone || btwo || bthree || bfour || bfive || bsix || bseven)
                        {
                            result[0] = i;
                            fistFlag = false;
                        }
                    }else if(i+4>=len-1 && i+1<=len-1){
                        if(bwProjection[i] > 0){
                            result[0] = i;
                            fistFlag = false;
                        }
                    }

                }
                if ((bwProjection[end - k] >0) && lastFlag)
                {

                    if (blTwo && k <= 2)
                    {
                        result[1] = end - k;
                        lastFlag = false;
                    }
                    else if (i + 4 <= len - 1)
                    {
                        lastfirstSum = bwProjection[end - k];
                        lastsecondSum = bwProjection[end - k-1];
                        lastthreeSum = bwProjection[end - k - 2];
                        lastfourSum = bwProjection[end - k - 3];
                        lastfiveSum = bwProjection[end - k-4];
                        boolean bone = lastfirstSum >= dt && lastsecondSum >= 1 && lastthreeSum >= 1 && lastfourSum >= 1 && lastfiveSum >= 1;
                        boolean btwo = lastfirstSum >= dt + 6 && lastsecondSum >= dt + 6;
                        boolean bthree = lastfirstSum >= dt && lastsecondSum >= dt && lastthreeSum >= dt && lastsecondSum>0;
                        boolean bfour = lastfirstSum > dt && lastsecondSum >= dt && lastthreeSum >= th  && lastsecondSum>0;
                        boolean bfive = lastfirstSum > dt && lastsecondSum >= dt && k > 5;
                        boolean bsix = k > len / 3 && bwProjection[end-k] > 0 && end - k - 1 > 0 && bwProjection[end - k-1] > 0;
                        boolean bseven =  Math.min(lastfirstSum,lastsecondSum)>0 && Math.max(lastfirstSum,lastsecondSum)>9 && Math.max(lastfirstSum,lastsecondSum)>6*dt;
                        if (bone || btwo || bthree || bfour || bfive || bsix || bseven)
                        {
                            result[1] = end - k;
                            lastFlag = false;
                        }
                    }else if(i+4>len-1 && i+2<=len-1){
                        if(bwProjection[end-k] > 0){
                            result[1] = end - k;
                            lastFlag = false;
                        }
                    }

                }
                k++;
                if (!lastFlag && !fistFlag) break;

            }
        }
        return result;
    }


    // 求投影
    public static int[] binaryProjection(Mat binary, int startX, int endX, int startY, int endY, boolean xAxis, int grayValue){
        int[] result = new int[xAxis ? binary.cols():binary.rows()];

        for (int i = startY; i < endY; i++)
        {
            for (int j = startX; j < endX; j++)
            {
                // 0xff
                if (binary.get(i,j)[0] == grayValue)
                {
                    if (xAxis)
                    {
                        //如果是X轴投影
                        result[j]++;
                    }
                    else
                    {
                        //如果是Y轴投影
                        result[i]++;
                    }
                }
            }
        }

        return result;
    }


    // get bounding points
    public static BinaryPointsEntity getBoundingPoints(Mat origin, Point offset, double whMaxRadio, double whMinRadio, int dtX, boolean isTie,boolean isNo,int minArea){

        BinaryPointsEntity bp = new BinaryPointsEntity();
        int width = origin.width();
        int height = origin.height();
        double radio = (double) width/(height);
        double bwRadio =getBwRadio(radio);
        Mat bw = getBestBinary( origin, bwRadio,offset,  whMaxRadio,  whMinRadio,isNo,minArea);
        // 中值滤波
        Imgproc.medianBlur(bw,bw,3);
        bp =   ContoursUtil.getBinaryBaseInfoByFindContours(bw, Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE, offset, whMaxRadio, whMinRadio,minArea);
        List<PointEntity> points = bp.getPoints();
        points = SortUtil.reorder(points,true,false);
        if(points.size()>1){
            bp.setPoints(points);
            updateBinaryPoints( bp,0, points.size()-1,width,height, whMaxRadio, whMinRadio,dtX,isTie);
        }
        if(bp!=null){
            bp.setImage(origin);
        }
        return bp;

    }

    // get best bw
    public static Mat getBestBinary(Mat origin,double bwRadio,Point offset, double whMaxRadio, double whMinRadio,boolean isNo, int minArea){

        Mat bw = BinaryUtil.otsuNew(origin);
        BinaryPointsEntity bp =   ContoursUtil.getBinaryBaseInfoByFindContours(bw, Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE, offset, whMaxRadio, whMinRadio,minArea);
        boolean isGood = isGoodBinary(bp,isNo);
        if(!isGood){
            bw = BinaryUtil.getBinaryImg(origin,bwRadio);
        }
        return bw;
    }

    // update  去除噪声区域，合并内包含矩形
    public static void  updateBinaryPoints(BinaryPointsEntity bp,int newStartIndex,int newEndIndex,int width,int height,double whMaxRadio,double whMinRadio,int dt,boolean isTie){

        PointsCoreEntity pc = bp.getPointsCore();
        List<PointEntity> points = bp.getPoints();
        List<PointEntity> newPoints = new ArrayList<>();
        if(points!=null && points.size()>0){
            int pointsLen = newEndIndex-newStartIndex+1;
            PointEntity pStart = points.get(newStartIndex);
            int xLeftMin = pStart.getxLeft(),xRightMax = 0, maxWidth = 0,minWidth = pStart.getWidth();
            int yTopMin =  pStart.getyTop(),yTopMax = 0,yButtonMin = pStart.getyButton(),yButtonMax = 0,maxHeight = 0,minHeight = pStart.getHeight();
            double sumWidth = 0,sumHeight = 0;
            double yTopSum = 0,yButtonSum = 0;
            int goodWordCount = 0, inputGoodCount = pc.getGoodWordCount();
            int goodYTopMin = pc.getGoodYTopMin(),goodYButtonMax = pc.getGoodYButtonMax();
            int newGoodYTopMin = height, newGoodYButtonMax =0;
            double goodSumWidth=0, goodSumHeight = 0,goodYTopSum=0,goodYButtonSum=0;
            double spaceSum = 0,badSpaceSum =0;
            int spaceCount = 0,badSpaceCount = 0;
            int minSpace = width,maxSpace = 0;
            double hAvg = pc.getAvgHeight();
            double wAvg = pc.getAvgDiff();
//            double radioAvg = hAvg>0 ? wAvg/hAvg:0;
            double yTopAvg = pc.getyTopAvg();
            double yButtonAvg = pc.getyButtonAvg();
            if(pointsLen>=1){
                for(int i =newStartIndex;i<=newEndIndex;i++){
                    PointEntity p = points.get(i);
                    int x1 = p.getxLeft(), x2 = p.getxRight();
                    int y1 =  p.getyTop(), y2 =  p.getyButton();
                    int w = p.getWidth(), h = p.getHeight();
                    double radio = p.getWhRadio();
                    int xr1 = 0, wr = 0, hr = 0,yr1 =0,yr2=0;
                    if(i+1<=newEndIndex){
                        PointEntity pr = points.get(i+1);
                        xr1 =  pr.getxLeft();
                        wr = pr.getWidth();
                        hr = pr.getHeight();
                        yr1 = pr.getyTop();
                        yr2 = pr.getyButton();
                    }
                    boolean isBadWidthOne  = w>5.08*hAvg && i+1<=newEndIndex && xr1<x1+2*hAvg && (y1>yr2 || y2<yr1);
                    boolean isBadWidthTwo = w>0.528*width  && h<0.568*height;
                    if(isBadWidthOne || isBadWidthTwo){
                        continue;
                    }
                    boolean isBadStartOne = inputGoodCount> 0  && ((y1>goodYButtonMax || y2<goodYTopMin &&  h<0.528*hAvg) || ((y1>yButtonAvg || y2<yTopAvg) && h<0.368*hAvg  && xr1<=x2 ));
                    dt = Math.min(3,dt);
                    boolean isPoint = w*h<=dt*dt || ((w<1 || h<=1 ) && w*h<=dt*dt+dt ) || ( (w<=2*dt || h<=2*dt) &&  ((y1>yButtonAvg-0.1*hAvg && y2<yButtonAvg )|| (y2<yTopAvg-0.1*hAvg && y1>yTopAvg)) && w*h<3*dt*dt);
                    boolean isBadUpper = i+1<=newEndIndex && w*h<3*dt*dt && w<=dt && xr1-x2<=dt && wr>0.868*wAvg && hr>0.868*hAvg && yr1-y2>0.898*hAvg;
                    boolean isBadUpperOrLowerPoint = (y2<yTopAvg-0.138*hAvg || y1>yButtonAvg+0.138*hAvg) && (w*h<=4*dt*dt || w*h<7);
                    boolean isBadUpperOrLowerPointOne = (y2<yTopAvg-0.298*hAvg || y1>yButtonAvg+0.298*hAvg) && w*h<=9 && h<=3;
                    if(isBadStartOne || isPoint || isBadUpper || isBadUpperOrLowerPoint || isBadUpperOrLowerPointOne){
                        continue;
                    }
                    int continueEnd = i;
                    // region x direction merge
                    List<PointEntity> overlappingList = new ArrayList<>();
                    if(i+1<=newEndIndex){
                        continueEnd = findXMaxOverlapping(points,i,newEndIndex,x2,wAvg);
                        overlappingList = overlappingAreaRecut(points,newPoints,i, continueEnd, newEndIndex, yTopAvg, yButtonAvg, hAvg, wAvg, goodYTopMin, goodYButtonMax,6);
                    }else {
                        overlappingList.add(p);
                    }
                    // endregion
                    if(overlappingList.size()<1){
                        i = continueEnd;
                        continue;
                    }
                    // region add overlapping info
                    boolean isCheck = true;
                    for(int k =0;k<overlappingList.size();k++){
                        PointEntity item = overlappingList.get(k);
                        x1 = item.getxLeft(); x2 = item.getxRight();y1 = item.getyTop();y2= item.getyButton();
                        radio = item.getWhRadio();h = item.getHeight();w = item.getWidth();
                        // region y check
                        if( isTie && (h<1.08*hAvg || (h<hAvg+1 && h<1.126*hAvg)) ){
                            if(h<0.658*hAvg){
                                y1 = Math.max(0,y1-(int)Math.round(0.628*(y1-goodYTopMin)));
                                y2 = Math.min(height-1,y2+ (int) Math.round(0.628*(goodYButtonMax-y2)));
                            }else if(h>=1.08*hAvg){
                                y1 = Math.max(0,y1-1);
                            } else{
                                y1 = Math.max(0,y1-3);
                                y2 = Math.min(y2+3,height-1);
                            }
                            h = y2-y1+1;
                            item.setyTop(y1);
                            item.setyButton(y2);
                            item.setHeight(h);
                            isCheck = false;
                        }
                        // endregion
                        if(k+1<overlappingList.size()){
                            xr1 =  overlappingList.get(k+1).getxLeft();
                        }
                        int tempRightSpace = xr1 - x2+1;
                        int spaceDiff = 0;
                        if(newPoints.size()>0){
                            PointEntity pl = newPoints.get(newPoints.size()-1);
                            int xl2 = pl.getxRight();
                            // region x check
                            if( isTie &&  w<0.708*wAvg ){
                                if(tempRightSpace>0.398*wAvg && spaceDiff>0.398*wAvg){
                                    isCheck = false;
                                    if(w<6 && tempRightSpace>(6-w)/2){
                                        int temp = (int) Math.round((6-w)*0.5);
                                        x1 = x1-temp;
                                        x2 = x2+temp;
                                    }else {
                                        x2 = x2+Math.max(1,(int)Math.round(0.198*tempRightSpace));
                                        x1 = x1-Math.max(1,(int)Math.round(0.198*spaceDiff));
                                    }
                                    w= x2-x1+1;
                                    item.setxLeft(x1);
                                    item.setxRight(x2);
                                    item.setWidth(w);
                                } else if(tempRightSpace>0.398*wAvg && tempRightSpace>spaceDiff){
                                    isCheck = false;
                                    x2 = x2+Math.max(1,(int)Math.round(0.298*tempRightSpace));
                                    w= x2-x1+1;
                                    if(w<6 && tempRightSpace>0.568*wAvg){
                                        if(spaceDiff>6-w){
                                            x1 = x1-6+w;
                                        }else{
                                            x2 = x2+6-w;
                                        }
                                        w= x2-x1+1;
                                    }
                                    item.setxRight(x2);
                                    item.setWidth(w);
                                }else if(spaceDiff>0.398*wAvg){
                                    isCheck = false;
                                    x1 = x1-Math.max(1,(int)Math.round(0.328*spaceDiff));
                                    w = x2-x1+1;
                                    if(w<6 && spaceDiff>0.568*wAvg ){
                                        if(x2+6-w<width){
                                            x2 = x2+6-w;
                                            item.setxRight(x2);
                                        }else {
                                            x1 = x1-6+w;
                                        }
                                        w = x2-x1+1;
                                    }
                                    item.setxLeft(x1);
                                    item.setWidth(w);
                                }
                            }
                            // endregion
                            spaceDiff = x1-xl2-1;
                            minSpace = Math.min(minSpace,spaceDiff);
                            maxSpace = Math.max(maxSpace,spaceDiff);
                            if(spaceDiff>0 && spaceDiff<0.5*(wAvg+hAvg)){
                                spaceSum += spaceDiff;
                                spaceCount++;
                            }else {
                                badSpaceSum++;
                                badSpaceCount++;
                            }
                        }
                        radio = (double)w/h;
                        item.setWhRadio(radio);
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
                        boolean isGood = (h>0.469*hAvg && hAvg>0 || (h>0.1*height && hAvg<=0));
                        if(radio<whMaxRadio && radio>whMinRadio && isGood ){
                            newGoodYTopMin = Math.min(newGoodYTopMin,y1);
                            newGoodYButtonMax = Math.max(newGoodYButtonMax,y2);
                            goodSumWidth += w;
                            goodSumHeight +=h;
                            goodYTopSum +=y1;
                            goodYButtonSum +=y2;
                            goodWordCount++;
                            boolean isGoodPoint = isCheck && Math.abs(h-hAvg)<0.178*hAvg && Math.abs(w-wAvg)<0.178*wAvg && spaceDiff>=0.168*wAvg && spaceDiff<0.419*wAvg;
                            item.setGood(isGoodPoint);
                        }

                        sumHeight += h;
                        sumWidth += w;
                        yTopSum += y1;
                        yButtonSum += y2;
                        newPoints.add(item);
                    }
                    // endregion
                    i = continueEnd;
                }

                // region get core info
                hAvg = sumHeight/pointsLen;
                wAvg = sumWidth/pointsLen;
                yTopAvg = yTopSum/pointsLen;
                yButtonAvg = yButtonSum/pointsLen;
                double spaceAvg =1;
                if(spaceCount>0){
                    spaceAvg = spaceSum/spaceCount;
                }else if(badSpaceCount>0){
                    spaceAvg = badSpaceSum/badSpaceCount;
                }
                pointsLen = newPoints.size();
                if(goodWordCount>0){
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
                // endregion
                pc = new PointsCoreEntity(pointsLen,spaceCount, maxWidth, minWidth,wAvg, spaceAvg,maxSpace,minSpace,badSpaceCount,badSpaceSum, xLeftMin, xRightMax, hAvg,
                        yTopAvg, yButtonAvg, yTopMin, yButtonMax, goodWordCount, sumWidth, sumHeight, minHeight, maxHeight,newGoodYTopMin,newGoodYButtonMax,false);
            }
        }
        bp.setPoints(newPoints);
        bp.setPointsCore(pc);
    }

    // 获取X方向重叠区域最大点
    public static int findXMaxOverlapping(List<PointEntity> points, int startIndex, int newEndIndex, int xt2,double wAvg ){

        int result = startIndex;
        for(int j = startIndex+1;j<=newEndIndex;j++){
            PointEntity pt = points.get(j);
            int w = pt.getWidth();
            double whRadio = pt.getWhRadio();
            int xt1 = pt.getxLeft();
            int offset =1;
            if(xt1-xt2<=3 && whRadio<0.598 && w<wAvg && wAvg>3.98*(xt1-xt2)){
                offset =2;
            }
            if(xt1>xt2+offset){
                break;
            }
            int rtx2 = pt.getxRight();
            xt2 = Math.max(rtx2,xt2);
            result = j;
        }
        return result;
    }

    // 对X方向重叠区域再分割
    public static List<PointEntity>  overlappingAreaRecut(List<PointEntity> points,List<PointEntity> newPoints,int startIndex,int endIndex,int newEndIndex,double yTopAvg,double yButtonAvg,double hAvg,double wAvg,int goodYTopMin,int goodYButtonMax,int maxCount){

        List<PointEntity> list = new ArrayList<>();
        PointEntity pc = points.get(startIndex);
        int xl2 = 0;
        int xEnd1 = 0;
        int x1 = pc.getxLeft(),x2 = pc.getxRight(),y1 = pc.getyTop(),y2 = pc.getyButton(),h = pc.getHeight(),w = pc.getWidth();
        double whRadio = pc.getWhRadio();
//        int endIndex = findXMaxOverlapping(points,startIndex,newEndIndex,x2);
        double dt = (yTopAvg-goodYTopMin +goodYButtonMax-yButtonAvg)/2;
        if(endIndex+1<=newEndIndex){
            xEnd1 = points.get(endIndex+1).getxLeft();
        }
        int npCount = newPoints.size();
        if(npCount>0){
            xl2 = newPoints.get(npCount-1).getxRight();
        }
        int diffCount = newEndIndex-endIndex-(startIndex-npCount);
        if(endIndex>startIndex){
            // region get near  topAvg and buttonAvg and spaceAvg
            int nearCount = 0,spaceCount =0,leftCount =0,rightCount =0;
            boolean isGoodMaxCount = false;
            double nearYTopAvg =0,nearYButtonAvg = 0,spaceAvg =0;
            int k = npCount-1;
            int j = endIndex+1;
            for(int i=0;i<=newEndIndex;i++){
                // region left get avg
                if(k>=0){
                    PointEntity p = newPoints.get(k);
                    int tempY1 = p.getyTop(), tempY2 = p.getyButton(),tempX1 = p.getxLeft(),tempH = p.getHeight();
                    if( tempY1>=goodYTopMin && tempY1<yTopAvg+dt  && tempY2<=goodYButtonMax && tempY2>yButtonAvg-dt  && tempH>0.568*hAvg){
                        nearYTopAvg +=tempY1;
                        nearYButtonAvg +=tempY2;
                        leftCount++;
                        if( k-1>=0){
                            int lx2 = newPoints.get(k-1).getxRight();
                            int spaceDiff  = tempX1 - lx2-1;
                            if(spaceDiff>0 && spaceAvg<0.5*(wAvg+hAvg)){
                                spaceAvg +=spaceDiff;
                                spaceCount ++;
                            }
                        }
                    }
                }
                // endregion  a
                // region right get avg
                if(j<=newEndIndex){
                    PointEntity p = points.get(j);
                    int cy1 = p.getyTop(),cy2 = p.getyButton(),cx2 = p.getxRight(),ch = p.getHeight();
                    if( cy1>=goodYTopMin && cy1<yTopAvg+dt && cy2<=goodYButtonMax && cy2>=yButtonAvg-dt && ch>0.5268*hAvg ){
                        nearYTopAvg +=cy1;
                        nearYButtonAvg +=cy2;
                        rightCount++;
                        if( j+1<=newEndIndex){
                            int rx1= points.get(j+1).getxLeft();
                            int spaceDiff  = rx1 - cx2-1;
                            if(spaceDiff>0 && spaceAvg<0.5*(wAvg+hAvg)){
                                spaceAvg +=spaceDiff;
                                spaceCount ++;
                            }
                        }
                    }
                }
                // endregion
                nearCount = rightCount+leftCount;
                if(!isGoodMaxCount && ((j>=newEndIndex && rightCount<1) || (k<1 && leftCount<1)) ){
                    maxCount = Math.max(3,maxCount-2);
                    isGoodMaxCount = true;
                }
                if(nearCount>=maxCount || (k<1  && j>=newEndIndex)){
                    break;
                }
                k--;
                j++;
            }
            if(nearCount>0){
                nearYTopAvg = nearYTopAvg/nearCount;
                nearYButtonAvg = nearYButtonAvg/nearCount;
            }else {
                nearYTopAvg = yTopAvg;
                nearYButtonAvg = yButtonAvg;
            }
            if(spaceCount>0){
                spaceAvg = spaceAvg/spaceCount;
            }
            // endregion
            // region find good point
            boolean isContinue = false;
            double diffRadio = 0.318;
            double maxDiffRadio = 0.3508;
            for(int i=startIndex+1;i<=endIndex;i++ ){
                PointEntity pt = points.get(i);
                PointEntity pr = null;
                PointEntity po = null;
                if(list.size()>0){
                    xl2 = list.get(list.size()-1).getxRight();
                }
                int cr = 0;
                if(i+1<=newEndIndex){
                    cr = points.get(i+1).getxLeft();
                }
                int tx1 = pt.getxLeft(),tx2 = pt.getxRight(),ty1 = pt.getyTop(),ty2 = pt.getyButton(),th = pt.getHeight(),tw = pt.getWidth();
                int maxX2 = Math.max(tx2,x2);
                boolean isRight = i+1<=endIndex;
                int rx1 = 0;
                if(isRight){
                    pr =  points.get(i+1);
                    rx1 = pr.getxLeft();
                    xEnd1 = rx1;
                }
                boolean isBadRight = ( (ty1>nearYButtonAvg-0.2*hAvg && ty2>nearYButtonAvg+0.15*hAvg )|| (ty2<nearYTopAvg+0.2*hAvg && ty1<nearYTopAvg-0.15*hAvg) );
                boolean isBadRightPoint =  (( (ty1>nearYButtonAvg-0.368*hAvg && ty2>nearYButtonAvg+0.1*hAvg) || (ty2<nearYTopAvg+0.368*hAvg  && ty1<nearYTopAvg-0.1*hAvg)) && (th<0.398*hAvg || w<0.368*wAvg)) || (th*tw<7 && (y2<nearYTopAvg+0.1*hAvg || y1>nearYButtonAvg-0.1*hAvg));
                double topDiffTwo = Math.abs(ty1-nearYTopAvg), buttonDiffTwo = Math.abs(ty2-nearYButtonAvg);
                boolean isGoodDiffTwo = ( topDiffTwo<diffRadio*hAvg && buttonDiffTwo<diffRadio*hAvg ) || (topDiffTwo+buttonDiffTwo<2*diffRadio*hAvg && buttonDiffTwo<maxDiffRadio*hAvg  && buttonDiffTwo<maxDiffRadio*hAvg );
                boolean isBadTwo = ( !isGoodDiffTwo && (isBadRight || isBadRightPoint)) || (th*tw<=6 && tw<=2);
                if(isContinue){
                    if(!isRight || (isRight && rx1>tx2)){
                        if(!isBadTwo){
                            list.add(pt);
                        }
                    }else if(rx1<=tx2){
                        pc = pt;
                        x1 = tx1; x2 = tx2;y1= ty1;y2 = ty2; h=th;
                        isContinue = false;
                    }
                    continue;
                }
                boolean isContinueOne = isRight && rx1<=x2;
                boolean isContinueTwo = isRight && rx1<=tx2;
                boolean isGoodCurrentRange =  y1>nearYTopAvg-0.15* hAvg &&  y2<=nearYButtonAvg+0.15*hAvg;
                boolean isGoodRightRange =  ty1>nearYTopAvg-0.15* hAvg &&  ty2<=nearYButtonAvg+0.15*hAvg;
                boolean isBadCurrent = (y1>nearYButtonAvg-0.2*hAvg || y2<nearYTopAvg+0.2*hAvg);
                boolean isBadCurrentPoint =  (((y1>nearYButtonAvg-0.368*hAvg && y2>nearYButtonAvg+0.1*hAvg )|| (y2<nearYTopAvg+0.368*hAvg && y1<nearYTopAvg-0.1*hAvg)) && (h<0.398*hAvg || w<0.368*wAvg)) || (th*tw<7 && (y2<nearYTopAvg+0.1*hAvg || y1>nearYButtonAvg-0.1*hAvg) ) ;
                boolean isBadCurrentOne = (y1>nearYButtonAvg+dt || y2<nearYTopAvg-dt);
                boolean isBadRightOne = (ty1>nearYButtonAvg+dt || ty2<nearYTopAvg-dt);
                if(isBadRightOne || isBadCurrentOne){
                    diffRadio = 0.409;
                }
                double topDiffOne = Math.abs(y1-nearYTopAvg), buttonDiffOne = Math.abs(y2-nearYButtonAvg);
                if(topDiffOne<0.1*hAvg && y2<=goodYButtonMax && tx1>=x2-3 && h<1.451*hAvg && w>0.808*wAvg){
                    maxDiffRadio = 0.409;
                }
                boolean isUpAndDown = isGoodCurrentRange && isGoodRightRange  && maxX2-x1<1.298*wAvg ;
                boolean isUpperOrButtonPoint = h>0.978*hAvg && ty1>nearYButtonAvg+2*dt || ty2<nearYTopAvg-2*dt && th*tw<7;
                boolean isGoodTop = h>1.5*th && Math.abs(h-hAvg)<0.25*hAvg && topDiffOne<0.469*hAvg && buttonDiffOne<0.469*hAvg;
                boolean isBadButton = ty1>nearYButtonAvg+0.469*hAvg;
                boolean isGoodDiffOne = ( topDiffOne<diffRadio*hAvg && buttonDiffOne<diffRadio*hAvg ) || (topDiffOne+buttonDiffOne<2*diffRadio*hAvg && topDiffOne<maxDiffRadio*hAvg && buttonDiffOne<maxDiffRadio*hAvg );
                boolean isGoodOne =  (isBadRight || isBadRightPoint ) && (isGoodDiffOne || (isGoodTop && isBadButton) )&& (!isUpAndDown || isUpperOrButtonPoint);
                boolean isGoodTwo =  (isBadCurrent || isBadCurrentPoint) && isGoodDiffTwo && !isUpAndDown;
                boolean isBadOne = !isGoodDiffOne && (isBadCurrent || isBadCurrentPoint);
                boolean isBadThree = (diffCount<=3 && maxX2-x1<=0.368*wAvg && cr-xl2<2.98*spaceAvg) || ( cr-xl2>0 && cr-xl2<1.678*spaceAvg ) || (maxX2-x1<0.5*spaceAvg && maxX2-x1<0.268*wAvg);
                boolean isBad = ((ty1>nearYButtonAvg || ty2<nearYTopAvg) &&  (y1>nearYButtonAvg || y2<nearYTopAvg) && ( xl2>0 && xEnd1>0 && xEnd1-xl2<0.568*wAvg ) || (isBadOne && isBadTwo && isBadThree));
                boolean isMergeGood = tx1>=x2-2 && tx1<=x2 && w>0.868*wAvg && th>0.828*wAvg && isGoodDiffOne && isGoodDiffTwo;

                if(isGoodOne){
                    if(!isContinueOne ){
                        list.add(pc);
                        isContinue = true;
                    }
                }else if(isGoodTwo){
                    if( !isContinueTwo){
                        list.add(pt);
                        isContinue = true;
                    }
                    pc = pt;
                    x1 = tx1; x2 = tx2;y1=ty1;y2 = ty2; h=th;
                }else if(isBad){
                    isContinue = true;
                }else if(isMergeGood){
                    if(tw>w && tx1==tx2){
                        if(tw>1.108*wAvg){
                            tx1 = tx1+Math.min(4,Math.max((int)(th-1.218*wAvg),2));
                        }else{
                            tx1 = tx1+1;
                        }
                    }else if(tw<w || tx1<tx2){
                        if( w>1.108*wAvg){
                            x2 = x2-Math.min(4,Math.max((int)(h-1.218*wAvg),2));
                        }else{
                            x2 = x2-1;
                        }
                        x2 = Math.min(tx1-1,x2);
                        pc.setxRight(x2);
                        pc.setWidth(x2-x1+1);
                        pc.setWhRadio((double)(x2-x1+1)/h);
                    }
                    list.add(pc);
                    x1 = tx1; x2 = tx2; y1 = ty1; y2 = ty2;w = tw; h = th;
                    pc =  new PointEntity( x1, y1,x2,y2,w,h,whRadio);
                    if(!isRight && !isBadTwo){
                        list.add(pc);
                    }
                } else{
                    x2 = maxX2;
                    y1 = Math.min(ty1,y1);
                    y2 = Math.max(ty2,y2);
                    if(y1>nearYTopAvg+0.15*nearYButtonAvg){
                        y1 = (int)Math.floor(nearYTopAvg);
                    }
                    if(y2<nearYButtonAvg-0.15*hAvg){
                        y2 = (int) Math.ceil(nearYButtonAvg);
                    }
                    w = x2-x1+1;
                    h = y2-y1+1;
                    whRadio = (double) w/h;
                    pc = new PointEntity( x1, y1,x2,y2,w,h,whRadio);
                    if(!(isContinueOne || isContinueTwo)){
                        list.add(pc);
                        isContinue = true;
                    }
                }
            }
            // endregion
        }else {
            boolean isBad =  (y1>goodYButtonMax || y2<goodYTopMin) && ( xl2>0 && xEnd1>0 && xEnd1-xl2<0.568*wAvg ) ;
            if(!isBad){
                list.add(pc);
            }
        }
        return list;
    }




    private static double getBwRadio(double radio){
        double bwRadio = 0.398;
        if(radio>7.98){
            bwRadio = 0.518;
        }else if(radio>5.68){
            bwRadio = 0.469;
        }
        return bwRadio;
    }

    private static boolean isGoodBinary(BinaryPointsEntity bp,boolean isNo){
        boolean isGood = false;
        if(bp!=null && bp.getPointsCore()!=null ){
            PointsCoreEntity pc = bp.getPointsCore();
            int count = bp.getPoints().size();
            int xStart = pc.getxStart(), xEnd = pc.getxEnd();
            int yStart = pc.getyStart(), yEnd = pc.getyEnd();
            int w = xEnd-xStart+1;
            int h = yEnd-yStart+1;
            double whRadio = w/(double)h;
            int maxDiff = pc.getMaxDiff();
            double hAvg = pc.getAvgHeight();
            boolean bone = ((( !isNo && maxDiff<2.98*hAvg) || (isNo && maxDiff<1.98*hAvg)) && count>0.6*whRadio) || maxDiff<1.68*hAvg;
            if(bone){
                isGood = true;
            }
        }
        return isGood;
    }



}
