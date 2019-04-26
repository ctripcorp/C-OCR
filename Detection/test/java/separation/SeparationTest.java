package separation;

import debug.DebugUtil;
import debug.ReadTxtData;
import entity.Constants;
import entity.PointEntity;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SeparationTest {

    static {
        //使用三方打包好的自适应系统load opencv动态链接库的包
        nu.pattern.OpenCV.loadShared();
        Constants.debugSwitch = true;
    }

    @Test
    public void testSeparation(){

        boolean isDebug = false;
        boolean debugSwitch = true;
        double whMaxRadio = 1.08, whMinRadio = 0.469;
        int dtX = 2;
        int minArea = 4;
        boolean isNo = true;
        String path =  this.getClass().getClassLoader().getResource("separation/").getPath();  // "F:\\ocr\\reject\\ImgSmall\\";
        path = path.substring(1, path.length());
        String outPath = "F:\\ocr\\reject\\test\\"; // you our effective path
        File file = new File(path);
        String type = "png";
        String fn = "3";
        List<String> list;
        if(isDebug){
            list = new ArrayList<>();
            list.add(fn+"."+type);
        }else {
            list = ReadTxtData.getFileNames(file, type,"","");
        }
        if(list!= null && list.size()>0){
            for(String str:list){
                fn = str.substring(0,str.lastIndexOf("."));
                Mat origin = Imgcodecs.imread(path + str);
                try {
                    List<PointEntity> points = SeparationBiz.separaion(origin, isNo, whMaxRadio, whMinRadio, dtX,minArea);
                    DebugUtil.lineCutPoints(origin,points,fn,debugSwitch, outPath);
                } catch (Exception e) {
                    System.out.println("exception fn="+fn);
                }
            }
        }

    }

}
