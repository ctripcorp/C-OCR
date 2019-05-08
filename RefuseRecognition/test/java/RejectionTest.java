import code.RejectBiz;
import debug.ReadTxtData;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RejectionTest {

    static {
        //使用三方打包好的自适应系统load opencv动态链接库的包
        nu.pattern.OpenCV.loadShared();
    }


    @Test
    public void testReject() throws Exception {
        boolean isDebug = false;
        String path =  this.getClass().getClassLoader().getResource("img/").getPath();  // "F:\\ocr\\reject\\ImgSmall\\";
        path = path.substring(1, path.length());
        File file = new File(path);
        String type = "png";
        String fn = "32";
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
                int rejectFlag = 0;
                try {
                    rejectFlag = RejectBiz.getRejectFlag(origin);
                } catch (Exception e) {
                   System.out.println("exception fn="+fn);
                }
                if(rejectFlag==0){
                    System.out.println(fn+" rejectFlag="+rejectFlag);
                }
            }
        }
    }





}
