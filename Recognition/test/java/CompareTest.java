import compare.HanmiCompare;
import compare.XorCompare;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static compare.DigestCompare.compare;
import static compare.DigestCompare.getData;

public class CompareTest {
    private static final Logger logger = LoggerFactory.getLogger(CompareTest.class);
    static{
        //使用三方打包好的自适应系统load opencv动态链接库的包
        nu.pattern.OpenCV.loadShared();
    }
    @Test
    //直方图比较
    public void DigestCompareTest() {
        String path1 = this.getClass().getClassLoader().getResource("0/38-26-cut-codeRow2-26.jpg").getPath();
        int length1 = path1.length();
        String path2 = this.getClass().getClassLoader().getResource("0/38-32-cut-codeRow2-32.jpg").getPath();
        int length2 = path2.length();

        String p1 =  path1.substring(1,length1);
        String p2 =  path2.substring(1,length2);
        float percent = compare(getData(p1),getData(p2));
        if (percent == 0) {
            System.out.println("无法比较");
        } else {
            System.out.println("两张图片的相似度为：" + percent + "%");
        }
    }


    @Test
    //异或比较
    public void XorCompareTest(){
        String path1 = this.getClass().getClassLoader().getResource("0/38-26-cut-codeRow2-26.jpg").getPath();
        int length1 = path1.length();
        String path2 = this.getClass().getClassLoader().getResource("0/38-32-cut-codeRow2-32.jpg").getPath();
        int length2 = path2.length();
        String p1 =  path1.substring(1,length1);
        String p2 =  path2.substring(1,length2);
        Mat origin1 = Imgcodecs.imread(p1);//779
        Mat origin2 = Imgcodecs.imread(p2);//779
        Mat binaryImg1 = XorCompare.getBinaryImg(origin1,25,40);
        Mat binaryImg2 = XorCompare.getBinaryImg(origin2,25,40);
        byte[] b1 = XorCompare.getBinaryByteArr(binaryImg1);
        byte[] b2 = XorCompare.getBinaryByteArr(binaryImg2);

        int relation =XorCompare.DoXorCompare(b1,b2);
        float percent = 1- (float)relation / (25*40);
        System.out.println("两张图片的相似度为：" + percent + "%");
    }

    @Test
    //海明比较
    public void HanmiCompareTest(){
        String path1 = this.getClass().getClassLoader().getResource("0/38-26-cut-codeRow2-26.jpg").getPath();
        int length1 = path1.length();
//      String path2 = this.getClass().getClassLoader().getResource("2/2-20-cut-codeRow2-20.jpg").getPath();
        String path2 = this.getClass().getClassLoader().getResource("0/38-32-cut-codeRow2-32.jpg").getPath();
        int length2 = path2.length();
        String p1 =  path1.substring(1,length1);
        String p2 =  path2.substring(1,length2);

        HanmiCompare p = new HanmiCompare();
        String image1;
        String image2;
        try {
            image1 = p.getHash(new FileInputStream(new File(p1)));
            image2 = p.getHash(new FileInputStream(new File(p2)));
            System.out.println("1:1 Score is " + p.distance(image1, image2));

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }




}
