package compare;

import entity.BinaryFeatureEntity;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *xor比较
 */
public class XorCompare {
    public static int[] Onetable = { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8 };
    /**
     *异或比较
     * java 程序作出的byte数据和C#里面的 模板 进行 比较会有问题
     * java里一个byte取值范围是-128~127, 而C#里一个byte是0~255.
     * **/
    public static int DoXorCompare(byte[] img1,byte[] img2){
        int relations = 0;
        try {
            int len = Math.min(img1.length,img2.length);
            for(int i=0;i<len;i++){
                byte r = (byte)(img1[i] ^ img2[i]);
                relations +=  Onetable[r & 0xFF];
            }
        }
        catch (Exception ex){
            System.out.print(ex.toString());
        }
        return relations;
    }

    /**
     * 获取图像二值特征byte[]
     */
    public static  byte[]  getBinaryByteArr(Mat binaryImg){
        int h =binaryImg.rows();
        int w = binaryImg.cols();
        int[][] bp01Int = new int[h][w];
        int flag = 0;
        int count = 0;
        StringBuilder letterImgSb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        byte[] letterimage = new byte[h*w/8];
        for(int i=0;i<h;i++){
            for(int j=0;j<w;j++){
                if(binaryImg.get(i,j)[0] == 0xff) {
                    bp01Int[i][j] = 0;
                    sb.append(0);
                }
                else{
                    bp01Int[i][j] = 1;
                    sb.append(1);
                }
                letterimage[flag] = (byte)((letterimage[flag] << 1) + bp01Int[i][j] );
                count++;
                if (count == 8)
                {
                    int val =  (int)letterimage[flag] ;
                    letterImgSb.append(val).append(" ");
                    flag++;
                    count = 0;
                }
            }
            sb.append("\n");
        }

        return letterimage;
    }


//
//    /**
//     * 获取图像二值特征byte[]
//     */
//    public static  String  getBinaryByteArr1(Mat binaryImg){
//        int h =binaryImg.rows();
//        int w = binaryImg.cols();
//        int[][] bp01Int = new int[h][w];
//        int flag = 0;
//        int count = 0;
//        StringBuilder letterImgSb = new StringBuilder();
//        StringBuilder sb = new StringBuilder();
//        byte[] letterimage = new byte[h*w/8];
//        for(int i=0;i<h;i++){
//            for(int j=0;j<w;j++){
//                if(binaryImg.get(i,j)[0] == 0xff) {
//                    bp01Int[i][j] = 0;
//                    sb.append(0);
//                }
//                else{
//                    bp01Int[i][j] = 1;
//                    sb.append(1);
//                }
//                letterimage[flag] = (byte)((letterimage[flag] << 1) + bp01Int[i][j] );
//                count++;
//                if (count == 8)
//                {
//                    int val =  (int)letterimage[flag] ;
//                    letterImgSb.append(val).append(",");
//                    flag++;
//                    count = 0;
//                }
//            }
//            sb.append("\n");
//        }
//
//        return letterImgSb.toString();
//    }


    /**
     * 图获取图像二值图像
     */
    public static Mat getBinaryImg(Mat img, int width, int height)
    {
        Imgproc.resize(img,img,new Size(width,height));
        Mat binaryImg = img.clone();
        Mat binaryImg1 = img.clone();
        Imgproc.cvtColor(img,binaryImg,Imgproc.COLOR_RGB2GRAY);
        int h = img.height();
        if(img.height() % 2 !=1){
            h = img.height() - 1;
        }
        int w = img.width();
        if(img.width()%2 !=1){
            w= img.width()-1;
        }
        int blockSize = Math.max(w,h);
        double stdevVal = (double) blockSize / 6;
        Imgproc.adaptiveThreshold(binaryImg,binaryImg1,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY_INV,blockSize,stdevVal);
        return binaryImg1;
    }
}
