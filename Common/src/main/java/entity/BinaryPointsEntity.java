package entity;

import org.opencv.core.Mat;

import java.util.List;

public class BinaryPointsEntity {

    private List<PointEntity> points;

    private PointsCoreEntity pointsCore;

    private Mat bwImage;

    private Mat image;

    private int brightSum;

    public int getBrightSum() {
        return brightSum;
    }

    public void setBrightSum(int brightSum) {
        this.brightSum = brightSum;
    }

    public List<PointEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointEntity> points) {
        this.points = points;
    }

    public PointsCoreEntity getPointsCore() {
        return pointsCore;
    }

    public void setPointsCore(PointsCoreEntity pointsCore) {
        this.pointsCore = pointsCore;
    }

    public Mat getBwImage() {
        return bwImage;
    }

    public void setBwImage(Mat bwImage) {
        this.bwImage = bwImage;
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }
}
