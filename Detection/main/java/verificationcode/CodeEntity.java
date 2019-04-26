package verificationcode;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;

/**
 * Created by jiangpeng on 2019/04/03.
 */
public class CodeEntity {
    private String type;    // number   caps    lower  handle
    private int sequence;    //序号
    private String value;    //识别值
    private String newValue;    //矫正值
    private Mat mat;
    private Rect rect;
    private RotatedRect rotatedRect; //最小外接矩形

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public RotatedRect getRotatedRect() {
        return rotatedRect;
    }

    public void setRotatedRect(RotatedRect rotatedRect) {
        this.rotatedRect = rotatedRect;
    }
}
