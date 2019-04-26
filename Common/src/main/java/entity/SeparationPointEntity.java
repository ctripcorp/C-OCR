package entity;

import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by denghuang on 2018/5/14.
 */
public class SeparationPointEntity {


    public List<PointLocationEntity> pointLocations;

    public List<Integer> listDiff;

    public BinaryProjectionEntity binaryProjection;

    public int brightSum;

    public double whiteLen;

    public int whiteCount;

    public double blackLen;

    public int blackCount;

    public int maxDiff;

    public int maxDiffEndIndex;

    public int minDiff;

    public int minDiffEndIndex;

    public int blackMaxDiff;

    public int blackMaxDiffEndIndex;

    public int blackMinDiff;

    public int blackMinDiffEndIndex;

    public Mat subImage;

}
