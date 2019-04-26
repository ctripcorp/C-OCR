package util;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;



public class BinaryUtil {

    // bw ostu by opencv
    public static Mat otsuNew(Mat origin){
        Mat gray = origin.clone();
        if(origin.channels() != 1){
            Imgproc.cvtColor(gray,gray, Imgproc.COLOR_RGB2GRAY);
        }
        Imgproc.threshold(gray,gray,0,255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(gray,gray,125,255, Imgproc.THRESH_BINARY_INV);
        return gray;
    }


    // bw adap --  Imgproc.THRESH_BINARY_INV:same otsu(黑底白字） Imgproc.THRESH_BINARY:opposite otsu
    public static Mat adaptiveBinary(Mat img, int blockSize, double param, int binaryType){
        Mat input = img.clone();
        if(input.channels() != 1){
            Imgproc.cvtColor(input,input, Imgproc.COLOR_RGB2GRAY);
        }
        Mat bw = Mat.zeros(input.size(), CvType.CV_8UC1);
        Imgproc.adaptiveThreshold(input, bw,  255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C , binaryType, blockSize, param);
        return bw;

    }

    public static int getGraySum(int[] bwProjectionY){
        int result =0;
        if(bwProjectionY!=null && bwProjectionY.length>0){
            for(int i=0;i<bwProjectionY.length;i++){
                result += bwProjectionY[i];
            }
        }
        return result;
    }

    // 求区间文字像素和
    public static int IntervalSum(int start, int end, int[] bwProjection)
    {
        int sum = 0;
        if (bwProjection != null && bwProjection.length > 0 && start >= 0 && end - start > 0 && end < bwProjection.length)
        {
            for (int i = start; i <= end; i++)
            {
                sum = sum + bwProjection[i];
            }
        }
        return sum;
    }

    // bw by adaptive
    public static Mat getBinaryImg(Mat origin,double bwRadio){
        int width = origin.width();
        int height = origin.height();
        double radio = (double) width/(height);
//        double bwRadio = 0.398;
        int min = Math.min(height,width);
        int blockSize = min;
        if(radio>6.58){
            blockSize = 3*min;
        }else if(radio>2){
            blockSize = 2*min;
        }
        if(blockSize%2==0){
            blockSize = blockSize+1;
        }
        double param = Math.max(bwRadio*min,1);
        Mat bw = adaptiveBinary(origin,blockSize,param, Imgproc.THRESH_BINARY_INV);
        return bw;
    }






}
