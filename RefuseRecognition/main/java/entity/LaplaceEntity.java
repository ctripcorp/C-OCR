package entity;

import org.opencv.core.Mat;

public class LaplaceEntity {

    private  int [] hist;

    private Mat img;

    public LaplaceEntity(int[] hist, Mat img) {
        this.hist = hist;
        this.img = img;
    }

    public int[] getHist() {
        return hist;
    }

    public void setHist(int[] hist) {
        this.hist = hist;
    }

    public Mat getImg() {
        return img;
    }

    public void setImg(Mat img) {
        this.img = img;
    }
}
