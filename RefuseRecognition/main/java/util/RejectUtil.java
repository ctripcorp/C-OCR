package util;

import entity.LaplaceEntity;
import entity.LogParameter;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8UC1;

public class RejectUtil {


    /**
     * 获取二值化结果
     *
     * @param origin 原图
     * @return 二值化图
     */
    public static Mat getGray(Mat origin,int h,int w){

        Mat gray = Mat.zeros(origin.size(),CV_8UC1);
        for(int i = 0;i < h;i++) {
            for (int j = 0; j < w; j++) {
                double [] bgr = origin.get(i,j);
                gray.put(i,j,0.2989 * bgr[2] + 0.5870 * bgr[1] + 0.1140 * bgr[0]);
            }
        }
        return gray;

    }

    public  static LaplaceEntity lapalace(Mat gray,int N,int h,int w){
        //region laplace and hist
        int [] hist = new int[N];
        int [][] pos = new int[][]{{-1,-1},{0,-1},{1,-1}
                ,{-1,0},{0,0},{1,0}
                ,{-1,1},{0,1},{1,1}};
        int [] laplaceKernel = new int[]{0,1,0,1,-4,1,0,1,0};
        Mat laplace = Mat.zeros(gray.size(),CV_8UC1);
        for(int i = 0;i < h;i++){
            for(int j = 0;j < w;j++){
                hist[(int)gray.get(i,j)[0]]++;
                double sum = 0;
                for(int l = 0;l < pos.length;l++){
                    int y = i+pos[l][1],x = j+pos[l][0];
                    if(y < 0){
                        y = 0;
                    }else if(y > h-1){
                        y = h-1;
                    }
                    if(x < 0){
                        x = 0;
                    }else if(x > w-1){
                        x = w-1;
                    }
                    sum += laplaceKernel[l]*gray.get(y,x)[0];
                }
                laplace.put(i,j,sum);
            }
        }
        LaplaceEntity laplaceEntity = new LaplaceEntity(hist,laplace);
        return laplaceEntity;
        //endregion
    }

    /**
     * 获取边缘轮廓信息
     *
     * @param origin 原图
     * @return 边缘提取结果
     */
    public static LogParameter logEdge(Mat origin )throws Exception{
        double log_white_sum = 0;
        double [][] kernel = new double[][]{{5.57299566975e-05,0.000100511394989,0.000200890689778,0.000368574223248,0.000573337079345,0.000744509718837,0.000811157771381,0.000744509718837,0.000573337079345,0.000368574223248,0.000200890689778,0.000100511394989,5.57299566975e-05},{0.000100511394989,0.000238089107237,0.000525021356347,0.000960218526853,0.00143059145259,0.00177523254835,0.0018973159046,0.00177523254835,0.00143059145259,0.000960218526853,0.000525021356347,0.000238089107237,0.000100511394989},{0.000200890689778,0.000525021356347,0.00113142495554,0.0018973159046,0.00249061698669,0.00271458056651,0.00273398136096,0.00271458056651,0.00249061698669,0.0018973159046,0.00113142495554,0.000525021356347,0.000200890689778},{0.000368574223248,0.000960218526853,0.0018973159046,0.00266248877795,0.0024894667083,0.00146392942387,0.000845048980312,0.00146392942387,0.0024894667083,0.00266248877795,0.0018973159046,0.000960218526853,0.000368574223248},{0.000573337079345,0.00143059145259,0.00249061698669,0.0024894667083,3.60483852992e-05,-0.00396540108172,-0.00600959997947,-0.00396540108172,3.60483852992e-05,0.0024894667083,0.00249061698669,0.00143059145259,0.000573337079345},{0.000744509718837,0.00177523254835,0.00271458056651,0.00146392942387,-0.00396540108172,-0.0116081008558,-0.0153575929312,-0.0116081008558,-0.00396540108172,0.00146392942387,0.00271458056651,0.00177523254835,0.000744509718837},{0.000811157771381,0.0018973159046,0.00273398136096,0.000845048980312,-0.00600959997947,-0.0153575929312,-0.019899129723,-0.0153575929312,-0.00600959997947,0.000845048980312,0.00273398136096,0.0018973159046,0.000811157771381},{0.000744509718837,0.00177523254835,0.00271458056651,0.00146392942387,-0.00396540108172,-0.0116081008558,-0.0153575929312,-0.0116081008558,-0.00396540108172,0.00146392942387,0.00271458056651,0.00177523254835,0.000744509718837},{0.000573337079345,0.00143059145259,0.00249061698669,0.0024894667083,3.60483852992e-05,-0.00396540108172,-0.00600959997947,-0.00396540108172,3.60483852992e-05,0.0024894667083,0.00249061698669,0.00143059145259,0.000573337079345},{0.000368574223248,0.000960218526853,0.0018973159046,0.00266248877795,0.0024894667083,0.00146392942387,0.000845048980312,0.00146392942387,0.0024894667083,0.00266248877795,0.0018973159046,0.000960218526853,0.000368574223248},{0.000200890689778,0.000525021356347,0.00113142495554,0.0018973159046,0.00249061698669,0.00271458056651,0.00273398136096,0.00271458056651,0.00249061698669,0.0018973159046,0.00113142495554,0.000525021356347,0.000200890689778},{0.000100511394989,0.000238089107237,0.000525021356347,0.000960218526853,0.00143059145259,0.00177523254835,0.0018973159046,0.00177523254835,0.00143059145259,0.000960218526853,0.000525021356347,0.000238089107237,0.000100511394989},{5.57299566975e-05,0.000100511394989,0.000200890689778,0.000368574223248,0.000573337079345,0.000744509718837,0.000811157771381,0.000744509718837,0.000573337079345,0.000368574223248,0.000200890689778,0.000100511394989,5.57299566975e-05}};
        //        Mat origin = Imgcodecs.imread("D:\\ocr\\cccNew\\3.png");
        Imgproc.cvtColor(origin,origin,Imgproc.COLOR_RGB2GRAY);
        //        Mat gray = Mat.zeros(origin.size(),CV_8UC1);
        Mat filter = Mat.zeros(origin.size(),CV_32F);
        int K = 13,half = 6;
        double thre = 0.012;
        for(int i = 0;i < origin.rows();i++){
            for(int j = 0;j < origin.cols();j++){
                double sum = 0;
                for(int l = -half;l <= half;l++){
                    for(int m = -half;m <= half;m++){
                        int y = l + i,x = m + j;
                        if(y < 0){
                            y = 0;
                        }else if(y > origin.rows() - 1){
                            y = origin.rows() - 1;
                        }
                        if(x < 0){
                            x = 0;
                        }else if(x > origin.cols() - 1){
                            x = origin.cols() - 1;
                        }
                        sum += origin.get(y,x)[0]*kernel[l+half][m+half];
                    }
                }
                filter.put(i,j,sum/255);
            }
        }
        Mat log = Mat.zeros(filter.size(),CV_8UC1);
        for(int i = 1;i < filter.rows() - 1;i++){
            for(int j = 1;j < filter.cols() - 1;j++){
                if(Math.abs(filter.get(i,j)[0]-filter.get(i,j+1)[0]) > thre && filter.get(i,j)[0]*filter.get(i,j+1)[0] < 0){
                    log.put(i,j,0xff);
                }
                if(Math.abs(filter.get(i,j)[0]-filter.get(i+1,j)[0]) > thre && filter.get(i,j)[0]*filter.get(i+1,j)[0] < 0){
                    log.put(i,j,0xff);
                }
            }
        }
        for(int i = 0;i < filter.rows();i++){
            for(int j = 0;j < filter.cols();j++){
                if(log.get(i,j)[0] == 0){
                    if(j > 0 && j < filter.cols() - 1 && Math.abs(filter.get(i,j-1)[0]-filter.get(i,j+1)[0]) > 2* thre &&
                            filter.get(i,j-1)[0]*filter.get(i,j+1)[0] < 0){
                        log.put(i,j,0xff);
                    }
                    if(i > 0 && i < filter.rows() - 1 && Math.abs(filter.get(i+1,j)[0]-filter.get(i-1,j)[0]) > 2* thre &&
                            filter.get(i-1,j)[0]*filter.get(i+1,j)[0] < 0){
                        log.put(i,j,0xff);
                    }
                }
            }
        }
        LogParameter logParameter = getLogWhiteSum(log);
        return logParameter;
    }

    private  static LogParameter getLogWhiteSum(Mat log){
        LogParameter parameter = new LogParameter();
        double sum =0;
        double log_radio=0;
        int h = log.rows(),w = log.cols();
        for(int i = 0;i < h;i++){
            for(int j = 0;j < w;j++){
                if(log.get(i,j)[0] == 0xff)
                    sum++;
            }
        }
        log_radio =sum/(h*w);
        parameter.setLog_white_sum(sum);
        parameter.setLog_radio(log_radio);
        //        System.out.println("sum="+sum +" log_radio="+log_radio);
        return parameter;
    }

}
