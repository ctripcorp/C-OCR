import Feature.FeatureVector;
import Feature.ImageFeatureExtractor;
import compare.XorCompare;
import entity.CompareEntity;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import logistic.LogisticModel;
import util.JsonUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FeatureCompareTest {
    private static final Logger logger = LoggerFactory.getLogger(FeatureCompareTest.class);
static{
    //使用三方打包好的自适应系统load opencv动态链接库的包
    nu.pattern.OpenCV.loadShared();
}

    @Test
    public void doOcrHog(){
        String folder = this.getClass().getClassLoader().getResource("").getPath();
        int length = folder.length();
        folder=  folder.substring(1,length);
        FileUtil.readAllFile(folder);
        ArrayList<String> files =   FileUtil.files;
        List<Map<String,Object>> list0=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list2=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list3=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list4=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list5=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list6=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list7=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list8=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list9=new ArrayList<Map<String,Object>>();
        for(int i =0; i< files.size();i++) {
            try {
                String path = files.get(i);
                File f = new File(path);
                if (f.isFile() && f.exists()) {
                    FeatureVector featureVector = GetFeatureVector(path);
                    List<Double> list =featureVector.denseFeatures.get("hog");
                    double[] arr = new  double[list.size()];
                    for(int j =0; j<list.size();j++ ){
                        arr[j] = list.get(j);
                    }
                    Map<String,Object> map0=new HashMap<String,Object>();
                    map0.put("x",arr);
                    if(path.contains("\\0\\")) {
                        map0.put("y", 1.0);//结果为相似
                    }
                    else{
                        map0.put("y",0.0);//结果为不相似
                    }
                    list0.add(map0);
                    Map<String,Object> map1=new HashMap<String,Object>();
                    map1.put("x",arr);
                    if(path.contains("\\1\\")) {
                        map1.put("y", 1.0);//结果为相似
                    }
                    else{
                        map1.put("y",0.0);//结果为不相似
                    }
                    list1.add(map1);
                    Map<String,Object> map2=new HashMap<String,Object>();
                    map2.put("x",arr);
                    if(path.contains("\\2\\")) {
                        map2.put("y", 1.0);//结果为相似
                    }
                    else{
                        map2.put("y",0.0);//结果为不相似
                    }
                    list2.add(map2);

                    Map<String,Object> map3=new HashMap<String,Object>();
                    map3.put("x",arr);
                    if(path.contains("\\3\\")) {
                        map3.put("y", 1.0);//结果为相似
                    }
                    else{
                        map3.put("y",0.0);//结果为不相似
                    }
                    list3.add(map3);

                    Map<String,Object> map4=new HashMap<String,Object>();
                    map4.put("x",arr);
                    if(path.contains("\\4\\")) {
                        map4.put("y", 1.0);//结果为相似
                    }
                    else{
                        map4.put("y",0.0);//结果为不相似
                    }
                    list4.add(map4);

                    Map<String,Object> map5=new HashMap<String,Object>();
                    map5.put("x",arr);
                    if(path.contains("\\5\\")) {
                        map5.put("y", 1.0);//结果为相似
                    }
                    else{
                        map5.put("y",0.0);//结果为不相似
                    }
                    list5.add(map5);

                    Map<String,Object> map6=new HashMap<String,Object>();
                    map6.put("x",arr);
                    if(path.contains("\\6\\")) {
                        map6.put("y", 1.0);//结果为相似
                    }
                    else{
                        map6.put("y",0.0);//结果为不相似
                    }
                    list6.add(map6);

                    Map<String,Object> map7=new HashMap<String,Object>();
                    map7.put("x",arr);
                    if(path.contains("\\7\\")) {
                        map7.put("y", 1.0);//结果为相似
                    }
                    else{
                        map7.put("y",0.0);//结果为不相似
                    }
                    list7.add(map7);

                    Map<String,Object> map8=new HashMap<String,Object>();
                    map8.put("x",arr);
                    if(path.contains("\\8\\")) {
                        map8.put("y", 1.0);//结果为相似
                    }
                    else{
                        map8.put("y",0.0);//结果为不相似
                    }
                    list8.add(map8);

                    Map<String,Object> map9=new HashMap<String,Object>();
                    map9.put("x",arr);
                    if(path.contains("\\9\\")) {
                        map9.put("y", 1.0);//结果为相似
                    }
                    else{
                        map9.put("y",0.0);//结果为不相似
                    }
                    list9.add(map9);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        //训练0-9
        LogisticModel lm0=new LogisticModel(list0);//带入模型
        lm0.go();
        LogisticModel lm1=new LogisticModel(list1);//带入模型
        lm1.go();
        LogisticModel lm2=new LogisticModel(list2);//带入模型
        lm2.go();
        LogisticModel lm3=new LogisticModel(list3);//带入模型
        lm3.go();
        LogisticModel lm4=new LogisticModel(list4);//带入模型
        lm4.go();
        LogisticModel lm5=new LogisticModel(list5);//带入模型
        lm5.go();
        LogisticModel lm6=new LogisticModel(list6);//带入模型
        lm6.go();
        LogisticModel lm7=new LogisticModel(list7);//带入模型
        lm7.go();
        LogisticModel lm8=new LogisticModel(list8);//带入模型
        lm8.go();
        LogisticModel lm9=new LogisticModel(list9);//带入模型
        lm9.go();

        //////////
        //测试集合
        String folderTest = this.getClass().getClassLoader().getResource("test").getPath();
        int lengthTest = folderTest.length();
        folderTest=  folderTest.substring(1,lengthTest);

        File file = new File(folderTest);		//获取其file对象
        File[] fs = file.listFiles();
        for(File f:fs){
            if(!f.isDirectory()){
                String p = f.getPath();
                double[] arr1 = getHogFeatureArr(p);
                double[] ret = new double[10];
                ret[0] =lm0.function(arr1);
                ret[1] =lm1.function(arr1);
                ret[2] =lm2.function(arr1);
                ret[3] =lm3.function(arr1);
                ret[4] =lm4.function(arr1);
                ret[5] =lm5.function(arr1);
                ret[6] =lm6.function(arr1);
                ret[7] =lm7.function(arr1);
                ret[8] =lm8.function(arr1);
                ret[9] =lm9.function(arr1);

                StringBuilder sb = new StringBuilder();
                double min = 0;
                int idx = 0;
                for(int i=0;i<=9;i++){
                    sb.append(i + ":" +ret[i]).append(" ");
                    if(min <ret[i]){
                        min = ret[i];
                        idx =i;
                    }
                }
                sb.append( f.getName()).append(" ");
                sb.append(" ret:" + idx).append("\n");
                System.out.println(sb.toString());
            }
        }
        ////////
    }


    @Test
    public void doXor(){
        String path = this.getClass().getClassLoader().getResource("templetes").getPath();
        String context = FileUtil.readFile(path);
        List<CompareEntity> templetes = JsonUtil.jsonToList(context,CompareEntity.class);

        String folderTest = this.getClass().getClassLoader().getResource("test").getPath();
        int lengthTest = folderTest.length();
        folderTest=  folderTest.substring(1,lengthTest);

        File file = new File(folderTest);		//获取其file对象
        File[] fs = file.listFiles();
        StringBuilder sb = new StringBuilder();
        for(File f:fs) {
            if (!f.isDirectory()) {
                String p = f.getPath();
                Mat origin1 = Imgcodecs.imread(p);//779
                Mat binaryImg1 = XorCompare.getBinaryImg(origin1,25,40);
                byte[] b1 = XorCompare.getBinaryByteArr(binaryImg1);
                String cname ="";
                double min = 9999;
                for(int i = 0; i< templetes.size();i++){
                    CompareEntity temp = templetes.get(i);

                    String[] arr  =temp.getTemp().split(",");
                    byte[] b2 = new byte[arr.length];
                    for (int i1 = 0; i1 < arr.length; i1++) {
                        int a = Integer.parseInt(arr[i1]);
                        b2[i1] = (byte)a;
                    }
                    int relation =XorCompare.DoXorCompare(b1,b2);
                    if(min >relation){
                        min =relation;
                        cname = temp.getCnname();
                    }
                }
                sb.append( f.getName()).append(" ").append(cname).append("\n");
            }
        }
        System.out.println(sb.toString());
    }



//
//    @Test
//    //特征区分
//    public void doOcrHog(){
//        String folder = this.getClass().getClassLoader().getResource("").getPath();
//        int length = folder.length();
//        folder=  folder.substring(1,length);
//
//        FileUtil.readAllFile(folder);
//        ArrayList<String> files =   FileUtil.files;
//        double[][] data = new double[ files.size()][189];
//        List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
//        for(int i =0; i< files.size();i++) {
//            try {
//                String path = files.get(i);
//                File f = new File(path);
//                if (f.isFile() && f.exists()) {
//                    FeatureVector featureVector = GetFeatureVector(path);
//                    List<Double> list =featureVector.denseFeatures.get("hog");
//                    Map<String,Object> map=new HashMap<String,Object>();
//                     double[] arr = new  double[list.size()];
//                    for(int j =0; j<list.size();j++ ){
//                        arr[j] = list.get(j);
//                    }
//                    map.put("x",arr);
//                    if(path.contains("\\2\\")) {
//                        map.put("y", 1.0);//结果为相似
//                    }
//                    else{
//                        map.put("y",0.0);//结果为不相似
//                    }
//                    list1.add(map);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        //训练0-9
//        LogisticModel lm=new LogisticModel(list1);//带入模型
//        lm.go();
//
//        //测试
//        String path1 = this.getClass().getClassLoader().getResource("test").getPath();
//        int length1 = path1.length();
//
//        //0
//        String path =  path1.substring(1,length1)+"/0.png";
//        double[] arr1 = getHogFeatureArr(path);
//        double rate =lm.function(arr1);
//        System.out.println("0 rate：" + rate );
//
//        //1
//        path =  path1.substring(1,length1)+"/1.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("1 rate：" + rate );
//
//        //2
//        path =  path1.substring(1,length1)+"/2.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("2 rate：" + rate );
//        //3
//        path =  path1.substring(1,length1)+"/3.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("3 rate：" + rate );
//
//        //4
//        path =  path1.substring(1,length1)+"/4.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("4 rate：" + rate );
//
//        //5
//        path =  path1.substring(1,length1)+"/5.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("5 rate：" + rate );
//        //6
//        path =  path1.substring(1,length1)+"/6.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("6 rate：" + rate );
//        //7
//        path =  path1.substring(1,length1)+"/7.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("7 rate：" + rate );
//        //8
//        path =  path1.substring(1,length1)+"/8.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("8 rate：" + rate );
//        //9
//        path =  path1.substring(1,length1)+"/9.png";
//        arr1 = getHogFeatureArr(path);
//        rate =lm.function(arr1);
//        System.out.println("9 rate：" + rate );
//
//    }


    public FeatureVector GetFeatureVector(String path){
        BufferedImage image1 = null;
        try {
            image1 = ImageIO.read(new File(path));
            ImageFeatureExtractor featureExtractor = ImageFeatureExtractor.getInstance();
            FeatureVector featureVector = featureExtractor.getFeatureVector(image1);
            return featureVector;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return  null;

    }

    public  double[] getHogFeatureArr(String path){
        FeatureVector f =  GetFeatureVector(path);
        List<Double> list =f.denseFeatures.get("hog");
        double[] arr = new  double[list.size()];
        for(int j =0; j<list.size();j++ ){
            arr[j] = list.get(j);
        }
        return arr;
    }

}
