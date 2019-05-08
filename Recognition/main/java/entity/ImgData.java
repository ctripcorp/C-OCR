package entity;

import java.awt.image.BufferedImage;

public class ImgData {
	public BufferedImage img=null;//图片数据
	public double[][] data=null;//图片矩阵数据
	public double[] shadowX=null,shadowY=null;//投影比对数据
	public double focusX=0.0,focusY=0.0;//重心比对数据
	public ImgData[] blockData=null;//9宫格切割后的数据
	public String re="";//识别结果
	public double rate=0.0;//识别正确率
	public ImgData beforeImgData=null;//相邻的上一图片
	public ImgData nextImgData=null;//相邻的下一图片
	public int nextSpace=0;//与下一图片的间距
}
