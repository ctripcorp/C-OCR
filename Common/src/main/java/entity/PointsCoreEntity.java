package entity;


public class PointsCoreEntity {

    private int pointLen;

    private int spaceCount;

    // goodWordCount 表示whRadio<maxRadio && whRadio<minRadio的文本框
    private int goodWordCount;

    // x direction
    private double sumWidth;

    private int maxDiff;

    private int minDiff;

    private double avgDiff;

    private double avgSpaceDiff;

    private int maxSpaceDiff;

    private int minSpaceDiff;

    private int badSpaceCount;

    private double badSpaceSum;

    private int xStart;

    private int xEnd;

    // y direction
    private double sumHeight;

    private int minHeight;

    private int maxHeight;

    private double avgHeight;

    private double yTopAvg;

    private double yButtonAvg;

    private int yStart;

    private int yEnd;

    private int goodYTopMin;

    private int goodYButtonMax;

    private boolean isGood;

    private int maxArea;

    private int maxAreaIndex;

    public int getMaxAreaIndex() {
        return maxAreaIndex;
    }

    public void setMaxAreaIndex(int maxAreaIndex) {
        this.maxAreaIndex = maxAreaIndex;
    }

    public int getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(int maxArea) {
        this.maxArea = maxArea;
    }

    public PointsCoreEntity(){}

    public PointsCoreEntity(int pointLen, int spaceCount, int maxDiff, int minDiff, double avgDiff, double avgSpaceDiff, int maxSpaceDiff, int minSpaceDiff, int badSpaceCount, double badSpaceSum, int xStart, int xEnd, double avgHeight, double yTopAvg, double yButtonAvg, int yStart, int yEnd,
                            int goodWordCount, double sumWidth, double sumHeight, int minHeight, int maxHeight, int goodYTopMin, int goodYButtonMax, boolean isGood){

        this.pointLen = pointLen;
        this.spaceCount = spaceCount;
        this.maxDiff = maxDiff;
        this.minDiff = minDiff;
        this.avgDiff = avgDiff;
        this.avgSpaceDiff = avgSpaceDiff;
        this.maxSpaceDiff = maxSpaceDiff;
        this.minSpaceDiff = minSpaceDiff;
        this.badSpaceCount = badSpaceCount;
        this.badSpaceSum = badSpaceSum;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.avgHeight = avgHeight;
        this.yTopAvg = yTopAvg;
        this.yButtonAvg = yButtonAvg;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.goodWordCount = goodWordCount;
        this.sumWidth = sumWidth;
        this.sumHeight = sumHeight;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.goodYTopMin = goodYTopMin;
        this.goodYButtonMax  = goodYButtonMax;
        this.isGood = isGood;
    }

    public int getPointLen() {
        return pointLen;
    }

    public void setPointLen(int pointLen) {
        this.pointLen = pointLen;
    }

    public int getSpaceCount() {
        return spaceCount;
    }

    public void setSpaceCount(int spaceCount) {
        this.spaceCount = spaceCount;
    }

    public int getGoodWordCount() {
        return goodWordCount;
    }

    public void setGoodWordCount(int goodWordCount) {
        this.goodWordCount = goodWordCount;
    }

    public double getSumWidth() {
        return sumWidth;
    }

    public void setSumWidth(double sumWidth) {
        this.sumWidth = sumWidth;
    }

    public int getMaxDiff() {
        return maxDiff;
    }

    public void setMaxDiff(int maxDiff) {
        this.maxDiff = maxDiff;
    }

    public int getMinDiff() {
        return minDiff;
    }

    public void setMinDiff(int minDiff) {
        this.minDiff = minDiff;
    }

    public double getAvgDiff() {
        return avgDiff;
    }

    public void setAvgDiff(double avgDiff) {
        this.avgDiff = avgDiff;
    }

    public double getAvgSpaceDiff() {
        return avgSpaceDiff;
    }

    public void setAvgSpaceDiff(double avgSpaceDiff) {
        this.avgSpaceDiff = avgSpaceDiff;
    }

    public int getMaxSpaceDiff() {
        return maxSpaceDiff;
    }

    public void setMaxSpaceDiff(int maxSpaceDiff) {
        this.maxSpaceDiff = maxSpaceDiff;
    }

    public int getMinSpaceDiff() {
        return minSpaceDiff;
    }

    public void setMinSpaceDiff(int minSpaceDiff) {
        this.minSpaceDiff = minSpaceDiff;
    }

    public int getBadSpaceCount() {
        return badSpaceCount;
    }

    public void setBadSpaceCount(int badSpaceCount) {
        this.badSpaceCount = badSpaceCount;
    }

    public double getBadSpaceSum() {
        return badSpaceSum;
    }

    public void setBadSpaceSum(double badSpaceSum) {
        this.badSpaceSum = badSpaceSum;
    }

    public int getxStart() {
        return xStart;
    }

    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public void setxEnd(int xEnd) {
        this.xEnd = xEnd;
    }

    public double getSumHeight() {
        return sumHeight;
    }

    public void setSumHeight(double sumHeight) {
        this.sumHeight = sumHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getAvgHeight() {
        return avgHeight;
    }

    public void setAvgHeight(double avgHeight) {
        this.avgHeight = avgHeight;
    }

    public double getyTopAvg() {
        return yTopAvg;
    }

    public void setyTopAvg(double yTopAvg) {
        this.yTopAvg = yTopAvg;
    }

    public double getyButtonAvg() {
        return yButtonAvg;
    }

    public void setyButtonAvg(double yButtonAvg) {
        this.yButtonAvg = yButtonAvg;
    }

    public int getyStart() {
        return yStart;
    }

    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    public int getyEnd() {
        return yEnd;
    }

    public void setyEnd(int yEnd) {
        this.yEnd = yEnd;
    }

    public int getGoodYTopMin() {
        return goodYTopMin;
    }

    public void setGoodYTopMin(int goodYTopMin) {
        this.goodYTopMin = goodYTopMin;
    }

    public int getGoodYButtonMax() {
        return goodYButtonMax;
    }

    public void setGoodYButtonMax(int goodYButtonMax) {
        this.goodYButtonMax = goodYButtonMax;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }


}
