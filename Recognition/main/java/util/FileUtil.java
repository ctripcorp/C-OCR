package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class FileUtil {
    public static ArrayList<String> files = new ArrayList<String>();

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void readAllFile(String filepath) {
        File file= new File(filepath);
        if(!file.isDirectory()){
            if(file.getName().endsWith(".jpg")) {
                files.add(file.getPath());
            }
        }else if(file.isDirectory()){
            String[] filelist=file.list();
            if(filelist!=null && filelist.length>0) {
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath);
                    if (!readfile.isDirectory()) {
                        if (file.getName().endsWith(".jpg")) {
                            files.add(readfile.getPath());
                        }
                    } else if (readfile.isDirectory()) {
                        readAllFile(filepath + "\\" + filelist[i]);//递归
                    }
                }
            }
        }
//        for(int i = 0;i<files.size();i++){
//            System.out.println(files.get(i));
//        }
    }


    public static void writeFile(String txt,String path) {
        try {
            File writeName = new File(path); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(txt); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return sb.toString();
    }


}
