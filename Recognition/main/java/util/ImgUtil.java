package util;

import entity.ImgData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImgUtil {
	private final static int WIDTH_DEFAULT = 32;
	private final static int HEIGHT_DEFAULT = 32;
	public final static int COUNT_DEFAULT = 0;
	public final static int COUNT_PIX = 1;
	public final static int COUNT_SHADOW = 2;
	public final static int COUNT_FOCUS = 3;
	private static final Color WHITE=new Color(255,255,255),BLACK=new Color(0,0,0);
	private static final Logger logger = LoggerFactory.getLogger(ImgUtil.class);
//	@Test
//	public void textToImg() throws IOException{//生成图片样本库
//		String text=Texts.TEXT;
//		for(int i=0,len=text.length();i<len;i++){
//			char[] data={text.charAt(i)};
//			BufferedImage img=new BufferedImage(25, 25, BufferedImage.TYPE_INT_RGB);
//			Graphics gOld=img.getGraphics();
//			Graphics g=gOld.create();
//			g.setColor(new Color(255,255,255));
//			g.fillRect(0, 0, 25, 25);
//			g.setColor(new Color(0,0,0));
//			g.setFont(new Font("微软雅黑", Font.PLAIN, 25));
//			g.drawChars(data, 0, 1, 0, 21);
//			g=gOld;
//			img = cutImageX(img,twoValueByDouble(img,127));
//			if("<>/\\|:\"*?.".indexOf(data[0])!=-1){
//				String name=formatFileName(data[0]+"");
//				System.out.println(name);
//				ImageIO.write(img, "jpg", new File("F:\\tmImg\\test1\\"+name+".jpg"));
//			}else{
//				ImageIO.write(img, "jpg", new File("F:\\tmImg\\test1\\"+data[0]+".jpg"));
//			}
//
//		}
//	}
	/*
	public ImgData formatImg(String uri) throws IOException {
		BufferedImage img = ImageIO.read(new File(uri));
		double bl=img.getWidth()/(img.getHeight()+0.0);
		if(bl>0.7){//如果长宽比>0.7则认为是汉字，就进行空白切割，否则是符号字母，不切割空白，避免丢失上下属性
			img = cutImage(img,twoValueByDouble(img,127));
		}
		img = scaleByPercentage(img,WIDTH_DEFAULT, HEIGHT_DEFAULT);
		double[][] data=twoValueByDouble(img,127);
		//bean
		ImgData re;
		re=getImgData(data);
		re.blockData=formatImgBlock(data);
		return re;
	}


	public ImgData formatImg(BufferedImage img) throws IOException {
		double bl=img.getWidth()/(img.getHeight()+0.0);
		if(bl>0.7){//如果长宽比>0.7则认为是汉字，就进行空白切割，否则是符号字母，不切割空白，避免丢失上下属性
			img = cutImage(img,twoValueByDouble(img,127));
		}
		img = scaleByPercentage(img,WIDTH_DEFAULT, HEIGHT_DEFAULT);
		double[][] data=twoValueByDouble(img,127);
		//bean
		ImgData re;
		re=getImgData(data);
		re.blockData=formatImgBlock(data);
		return re;
	}

	public ImgData formatImg(File file) throws IOException {
		//data
		BufferedImage img = ImageIO.read(file);
		double bl=img.getWidth()/(img.getHeight()+0.0);
		if(bl>0.7){//如果长宽比>0.7则认为是汉字，就进行空白切割，否则是符号字母，不切割空白，避免丢失上下属性
			img = cutImage(img,twoValueByDouble(img,127));
		}
		img = scaleByPercentage(img,WIDTH_DEFAULT, HEIGHT_DEFAULT);
		double[][] data=twoValueByDouble(img,127);
		//bean
		ImgData re;
		re=getImgData(data);
		re.blockData=formatImgBlock(data);
		return re;
	}*/

	public ImgData formatImg(ImgData imgD){
		double[][] data=imgD.data;
		BufferedImage img=imgD.img;
		double bl=img.getWidth()/(img.getHeight()+0.0);
		if(bl>0.7){//如果长宽比>0.7则认为是汉字，就进行空白切割，否则是符号字母，不切割空白，避免丢失上下属性
			img=cutImage(img,data);
		}
		img = scaleByPercentage(img,WIDTH_DEFAULT, HEIGHT_DEFAULT);
		data=twoValueByDouble(img,127);
		//imgD.img=img;
		imgD.data=data;
		imgD=getImgData(imgD);
		imgD.blockData=formatImgBlock(data);
		return imgD;
	}
	
	public ImgData formatLibImg(File f) throws IOException{
		ImgData imgD=new ImgData();
		BufferedImage img = ImageIO.read(f);
		imgD.img=img;
		double[][] data=twoValueByDouble(img,127);
		double bl=img.getWidth()/(img.getHeight()+0.0);
		if(bl>0.7){//如果长宽比>0.7则认为是汉字，就进行空白切割，否则是符号字母，不切割空白，避免丢失上下属性
			img=cutImage(img,data);
		}
		img = scaleByPercentage(img,WIDTH_DEFAULT, HEIGHT_DEFAULT);
		data=twoValueByDouble(img,127);
		//imgD.img=img;
		imgD.data=data;
		imgD=getImgData(imgD);
		imgD.blockData=formatImgBlock(data);
		return imgD;
	}
	public double[][] formatData(double[][] data){
		BufferedImage img=arrToImg(data);
		/*double bl=img.getWidth()/(img.getHeight()+0.0);
		if(bl>0.7){//如果长宽比>0.7则认为是汉字，就进行空白切割，否则是符号字母，不切割空白，避免丢失上下属性
			img=cutImage(img,data);
		}*/
		img = scaleByPercentage(img,WIDTH_DEFAULT, HEIGHT_DEFAULT);
		data=twoValueByDouble(img,127);
		return data;
	}
	
	/**
	 * @author Administrator
	 * @Description 获取图片的特征数据
	 * @param 图片数据对象
	 */
	public ImgData getImgData(ImgData re){
		double[][] data=re.data;
		double shadowSumData=0.0,dataO,focusXSum=0.0,focusYSum=0.0,focusSum=0.0,
				focusX=0.0,focusY=0.0;
		int dataWidthLength=data.length,dataHeigthLength=data[0].length;
		double[] shadowX=new double[dataWidthLength];
		double[] shadowY=new double[dataHeigthLength];
		for(int i=0;i<dataWidthLength;i++){
			shadowSumData=0.0;
			for(int j=0;j<dataHeigthLength;j++){
				dataO=data[i][j];
				//shadow
				shadowSumData+=dataO;
				shadowY[j]+=dataO;
				//focus
				if(dataO!=0){
					focusXSum+=j;
					focusYSum+=i;
					focusSum++;
				}
			}
			shadowX[i]=shadowSumData;
		}
		//focus
		if(focusSum<=0){
			focusX=0;
			focusY=0;
		}else{
			focusX=focusXSum/(dataHeigthLength*focusSum);
			focusY=focusYSum/(dataWidthLength*focusSum);
		}
		re.focusX=focusX;
		re.focusY=focusY;
		re.shadowX=shadowX;
		re.shadowY=shadowY;
		return re;
	}

	/**
	 * 切片后再格式化
	 * @param 图片二维数组
	 * @return 切片后的图片数据
	 * */
	public ImgData[] formatImgBlock(double[][] data){
		double[][][] dataR=blockCut(data);
		int len=dataR.length;
		ImgData[] re=new ImgData[len];
		for(int i=0;i<len;i++){
			re[i]=new ImgData();
			re[i].data=dataR[i];
			re[i]=getImgData(re[i]);
		}
		return re;
	}
	
	public double[] allMatch(ImgData dataBean,ImgData imgBean){
		double[] re={1,0,0,0,0,0,0},re1,re2={0.0,0.0,0.0},ree;
		ImgData[] dataR=dataBean.blockData;
		ImgData[] imgR=imgBean.blockData;
		int len=dataR.length;
		re1=match(dataBean, imgBean);
		for(int i=0;i<len;i++){
			ree=match(dataR[i], imgR[i]);
			re2[0]+=ree[0];
			re2[1]+=ree[1];
			re2[2]+=ree[2];
		}
		re2[0]/=len;
		re2[1]/=len;
		re2[2]/=len;
		re[1]=re1[0];
		re[2]=re1[1];
		re[3]=re1[2];
		re[4]=re2[0];
		re[5]=re2[1];
		re[6]=re2[2];
		return re;
	}
	
	public double[] allMatch(double[][] data,double[][] img){
		double[] re={1,0,0,0,0,0,0,0};
		double[][][] dataR=blockCut(data);
		double[][][] imgR=blockCut(img);
		re[1]=shadowMatch(data,img);
		re[2]=pixelMatch(data,img);
		re[3]=blockMatch(dataR,imgR,ImgUtil.COUNT_PIX);
		re[4]=blockMatch(dataR,imgR,ImgUtil.COUNT_SHADOW);
		re[5]=blockMatch(dataR,imgR,ImgUtil.COUNT_FOCUS);
		re[6]=blockMatch(dataR,imgR,ImgUtil.COUNT_DEFAULT);
		re[7]=focusMatch(data,img);
		return re;
	}
	
	public double[] allMatch2(double[][] data,double[][] img){
		double[] re={1,0,0,0,0,0,0},re1,re2={0.0,0.0,0.0},ree;
		double[][][] dataR=blockCut(data);
		double[][][] imgR=blockCut(img);
		re1=match(data, img);
		for(int i=0;i<16;i++){
			ree=match(dataR[i], imgR[i]);
			re2[0]+=ree[0];
			re2[1]+=ree[1];
			re2[2]+=ree[2];
		}
		re2[0]/=16;
		re2[1]/=16;
		re2[2]/=16;
		re[1]=re1[0];
		re[2]=re1[1];
		re[3]=re1[2];
		re[4]=re2[0];
		re[5]=re2[1];
		re[6]=re2[2];
		return re;
	}
	
	public double[] match(ImgData dataBean,ImgData imgBean){
		double[][] data=dataBean.data,img=imgBean.data;
		double[] re={0,0,0},shadowDataX=dataBean.shadowX,shadowDataY=dataBean.shadowY,
				shadowImgX=imgBean.shadowX,shadowImgY=imgBean.shadowY;
		double pixeCount=0.0,dataFocusX=dataBean.focusX,dataFocusY=dataBean.focusY,
				imgFocusX=imgBean.focusX,imgFocusY=imgBean.focusY;
		int dataWidthLength=data.length,dataHeigthLength=data[0].length;
		//pixe
		for(int i=0;i<dataWidthLength;i++){
			for(int j=0;j<dataHeigthLength;j++){
				if(img[i][j]==data[i][j]){
					pixeCount++;
				}
			}
		}
		//shadow
		double cX=countLengthMy(shadowDataX,shadowImgX,dataWidthLength);
		double cY=countLengthMy(shadowDataY,shadowImgY,dataHeigthLength);
		//focus
		double max0=Math.max(dataFocusX,imgFocusX),max1=Math.max(dataFocusY,imgFocusY);
		double c0=0,c1=0;
		if(max0!=0){
			c0=Math.abs(dataFocusX-imgFocusX)/max0;
		}
		if(max1!=0){
			c1=Math.abs(dataFocusY-imgFocusY)/max1;
		}
		
		re[0]=pixeCount/(dataWidthLength*dataHeigthLength);
		re[1]=(cX+cY)/2.0;
		re[2]=1-(c0+c1)/2;
		return re;
	}
	
	/*
	 * 优化比对
	 * 只用一次遍历，得出所有比对结果
	 * */
	public double[] match(double[][] data,double[][] img){
		double[] re={0,0,0},focusDataCount={0.0,0.0},focusImgCount={0.0,0.0};
		double pixeCount=0.0,shadowSumData=0.0,shadowSumImg=0.0,dataO,
				imgO,focusDataXSum=0.0,focusDataYSum=0.0,focusDataSum=0.0,
				focusImgXSum=0.0,focusImgYSum=0.0,focusImgSum=0.0;
		int dataWidthLength=data.length,dataHeigthLength=data[0].length;
		double[] dataX=new double[dataWidthLength];
		double[] dataY=new double[dataHeigthLength];
		double[] imgX=new double[dataWidthLength];
		double[] imgY=new double[dataHeigthLength];
		for(int i=0;i<dataWidthLength;i++){
			shadowSumData=0.0;
			shadowSumImg=0.0;
			for(int j=0;j<dataHeigthLength;j++){
				dataO=data[i][j];
				imgO=img[i][j];
				//pixe
				if(imgO==dataO){
					pixeCount++;
				}
				//shadow
				shadowSumData+=dataO;
				shadowSumImg+=imgO;
				dataY[j]+=dataO;
				imgY[j]+=imgO;
				//focus
				if(dataO!=0){
					focusDataXSum+=j;
					focusDataYSum+=i;
					focusDataSum++;
				}
				if(imgO!=0){
					focusImgXSum+=j;
					focusImgYSum+=i;
					focusImgSum++;
				}
			}
			dataX[i]=shadowSumData;
			imgX[i]=shadowSumImg;
		}
		//shadow
		double cX=countLengthMy(dataX,imgX,dataWidthLength);
		double cY=countLengthMy(dataY,imgY,dataHeigthLength);
		//focus
		if(focusDataSum<=0){
			focusDataCount[0]=0;
			focusDataCount[1]=0;
		}else{
			focusDataCount[0]=focusDataXSum/(dataHeigthLength*focusDataSum);
			focusDataCount[1]=focusDataYSum/(dataWidthLength*focusDataSum);
		}
		if(focusImgSum<=0){
			focusImgCount[0]=0;
			focusImgCount[1]=0;
		}else{
			focusImgCount[0]=focusImgXSum/(dataHeigthLength*focusImgSum);
			focusImgCount[1]=focusImgYSum/(dataWidthLength*focusImgSum);
		}
		double max0=Math.max(focusDataCount[0],focusImgCount[0]),max1=Math.max(focusDataCount[1],focusImgCount[1]);
		double c0=0,c1=0;
		if(max0!=0){
			c0=Math.abs(focusDataCount[0]-focusImgCount[0])/max0;
		}
		if(max1!=0){
			c1=Math.abs(focusDataCount[1]-focusImgCount[1])/max1;
		}
		
		re[0]=pixeCount/(dataWidthLength*dataHeigthLength);
		re[1]=(cX+cY)/2.0;
		re[2]=1-(c0+c1)/2;
		return re;
	}
	
	/*
	 * 基于像素的对比
	 * @return 相似度  eg:49/50
	 */
	public double pixelMatch(double[][] data,double[][] img){
		double sum=0.0,count=0.0;
		int dataWidthLength=data.length,dataHeigthLength;
		if(dataWidthLength>img.length){
			dataWidthLength=img.length;
		}
		for(int i=0;i<dataWidthLength;i++){
			dataHeigthLength=data[i].length;
			if(dataHeigthLength>img[i].length){
				dataHeigthLength=img[i].length;
			}
			for(int j=0;j<dataHeigthLength;j++){
				if(img[i][j]==data[i][j]){
					count++;
				}
				sum++;
			}
		}
		return count/sum;
	}
	
	/*
	 * 基于xy投影的对比
	 * @return 相似度  eg:0.88
	 */
	public double shadowMatch(double[][] data,double[][] img){
		double[] dataX=countRow(data, true);
		double[] dataY=countRow(data, false);
		double[] imgX=countRow(img, true);
		double[] imgY=countRow(img, false);
		/*double cX=vectorLength(dataX,imgX);
		double cY=vectorLength(dataY,imgY);
		return (cX+cY)/2;*/
		double cX=countLengthMy(dataX,imgX,data[0].length);
		double cY=countLengthMy(dataY,imgY,data.length);
		return (cX+cY)/2.0;
	}
	
	/*
	 * 基于块的对比（九宫格）   分为几个小块，再对每一个块进行匹配，对结果求平均值
	 * type:
	 * 	 COUNT_PIX:对每一块进行像素点匹配
	 * 	 COUNT_SHADOW:对每一块投影匹配
	 * 	   默认:统计每一块1的点数，得到两个向量，求向量相似度
	 * @return 相似度  eg:0.88
	 */
	public double blockMatch(double[][][] dataR,double[][][] imgR,int type){
		if(dataR==null || imgR==null){
			return 0;
		}
		double ree=0.0;
		switch (type) {
		case COUNT_PIX:
			for(int i=0;i<16;i++){
				ree+=pixelMatch(dataR[i], imgR[i]);
			}
			ree/=16;
			break;
		case COUNT_SHADOW:
			for(int i=0;i<16;i++){
				ree+=shadowMatch(dataR[i], imgR[i]);
			}
			ree/=16;
			break;
		case COUNT_FOCUS:
			for(int i=0;i<16;i++){
				ree+=focusMatch(dataR[i], imgR[i]);
			}
			ree/=16;
			break;
		default:
			double[] dr=new double[16],ir=new double[16];
			for(int i=0;i<16;i++){
				dr[i]=count(dataR[i]);
				ir[i]=count(imgR[i]);
			}
			ree=countLengthMy(dr,ir,16);
			break;
		}
		return ree;
	}
	
	/*
	 * 基于重心的对比   判断图形在位置上的相似，是集中在中间还是左上...
	 * @return 相似度  eg:49/50
	 */
	public double focusMatch(double[][] data,double[][] img){
		double[] dataCount=focusCount(data),imgCount=focusCount(img);
		double max0=Math.max(dataCount[0],imgCount[0]),max1=Math.max(dataCount[1],imgCount[1]);
		double c0=0,c1=0;
		if(max0!=0){
			c0=Math.abs(dataCount[0]-imgCount[0])/max0;
		}
		if(max1!=0){
			c1=Math.abs(dataCount[1]-imgCount[1])/max1;
		}
		return 1-(c0+c1)/2;
	}
	
	/*
	 * 将二维数组分割成4*4的二维数组
	 * @return 三维数组，包含16个二维数组 
	 * */
	private double[][][] blockCut(double[][] data){
		int wLen=data.length,hLen=data[0].length;
		int wBc=wLen/4;
		int hBc=hLen/4;
		if(wBc<1 || hBc<1){
			return null;
		}
		double[][][] r=new double[16][][];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				int nowW=i*wBc,nowH=j*hBc;
				double[][] newD=new double[wBc][hBc];
				for(int ii=0;ii<wBc;ii++){
					for(int jj=0;jj<hBc;jj++){
						newD[ii][jj]=data[nowW+ii][nowH+jj];
					}
				}
				r[i*4+j]=newD;
			}
		}
		for(int i=0;i<3;i++){
			int newW=wLen-3*wBc,nowW=3*wBc,nowH=i*hBc;
			double[][] newD=new double[newW][hBc];
			for(int ii=0;ii<newW;ii++){
				for(int jj=0;jj<hBc;jj++){
					newD[ii][jj]=data[nowW+ii][nowH+jj];
				}
			}
			r[12+i]=newD;
		}
		for(int i=0;i<3;i++){
			int newH=hLen-3*hBc,nowH=3*hBc,nowW=i*wBc;
			double[][] newD=new double[wBc][newH];
			for(int ii=0;ii<wBc;ii++){
				for(int jj=0;jj<newH;jj++){
					newD[ii][jj]=data[nowW+ii][nowH+jj];
				}
			}
			r[(i+1)*4-1]=newD;
		}
		double[][] newD=new double[wLen-3*wBc][hLen-3*hBc];
		for(int ii=0;ii<wLen-3*wBc;ii++){
			for(int jj=0;jj<hLen-3*hBc;jj++){
				newD[ii][jj]=data[3*wBc+ii][3*hBc+jj];
			}
		}
		r[15]=newD;
		return r;
	}

	/*
	 * 计算二维数组在行,列上的重心
	 * */
	private double[] focusCount(double[][] data){
		double re[]={0.0,0.0};
		int wLen=data.length,hLen=data[0].length;
		double xSum=0.0,ySum=0.0,sum=0.0;
		for(int i=0;i<wLen;i++){
			for(int j=0;j<hLen;j++){
				if(data[i][j]!=0){
					xSum+=j;
					ySum+=i;
					sum++;
				}
			}
		}
		if(sum<=0){
			re[0]=0;
			re[1]=0;
		}else{
			re[0]=xSum/(hLen*sum);
			re[1]=ySum/(wLen*sum);
		}
		return re;
	}
	
	/*
	 * 欧几里德距离，两个向量间的距离统计，根据投影，统计相似度
	 * @return 相似度[0-1],为1则完全相似  eg:0.89
	
	private double vectorLength(double[] data,double[] img){
		double sum=0;
		int len=data.length;
		for(int i=0;i<len;i++){
			sum+=(data[i]-img[i])*(data[i]-img[i]);
		}
		sum=Math.sqrt(sum);
		return 1/(1+sum);
	} */
	/*
	 * 投影距离距离，根据投影，统计相似度
	 * @return 相似度[0-1],为1则完全相似  eg:0.89
	
	private double countLength(double[] data,double[] img,int length){
		double sum=0;
		int len=data.length;
		for(int i=0;i<len;i++){
			sum+=((data[i]-img[i])/length)*((data[i]-img[i])/length);
		}
		return 1-sum;
	} */
	/*
	 * 投影距离距离，根据投影，统计相似度（原创）
	 * @return 相似度[0-1],为1则完全相似  eg:0.89
	 */
	private double countLengthMy(double[] data,double[] img,int length){
		double sum=0;
		int len=data.length;
		for(int i=0;i<len;i++){
			double max=Math.max(data[i],img[i]);
			if(max>0){
				sum+=(Math.abs(data[i]-img[i]))/max;
			}
		}
		return 1-(sum/length);
	}
	/*
	 * 计算投影   
	 * true关于x行的投影  false关于y列的投影
	 * @return 每行1的个数 eg:{10,2,3}
	 */
	private double[] countRow(double[][] data,boolean byRow){
		double[] re=null;
		double sum=0.0;
		int dataWidthLength=data.length,dataHeigthLength=data[0].length;
		if(byRow){
			re=new double[dataWidthLength];
			for(int i=0;i<dataWidthLength;i++){
				sum=0.0;
				for(int j=0;j<dataHeigthLength;j++){
					sum+=data[i][j];
				}
				re[i]=sum;
			}
		}else{
			re=new double[dataHeigthLength];
			for(int i=0;i<dataHeigthLength;i++){
				sum=0.0;
				for(int j=0;j<dataWidthLength;j++){
					sum+=data[j][i];
				}
				re[i]=sum;
			}
		}
		return re;
	}
	
	/*
	 * 统计1的个数
	 * 
	 */
	private double count(double[][] data){
		double sum=0.0;
		int dataWidthLength=data.length,dataHeigthLength=data[0].length;
		for(int i=0;i<dataWidthLength;i++){
			for(int j=0;j<dataHeigthLength;j++){
				sum+=data[i][j];
			}
		}
		return sum;
	}
	
	/**
     * 二值化
     * flag:阀值   大于为白  小于为黑
     
	private BufferedImage twoValue(BufferedImage bimg,int flag){
		int wid=bimg.getWidth();
		int hei=bimg.getHeight();
		for(int i=0;i<wid;i++){
			for(int j=0;j<hei;j++){
				Color c=new Color(bimg.getRGB(i, j));
				int avg=(c.getBlue()+c.getGreen()+c.getRed())/3;
				if(avg>flag){
					bimg.setRGB(i, j, Color.WHITE.getRGB());
				}else{
					bimg.setRGB(i, j, Color.black.getRGB());
				}
				
			}
		}
		return bimg;
	}*/
	
	/**
     * 二值化
     * flag:阀值   大于为0  小于为1
     */
	public double[][] twoValueByDouble(BufferedImage bimg,int flag){
		int wid=bimg.getWidth();
		int hei=bimg.getHeight();
		double[][] re=new double[hei][wid];
		for(int i=0;i<hei;i++){
			for(int j=0;j<wid;j++){
				Color c=new Color(bimg.getRGB(j, i));
				int avg=(c.getBlue()+c.getGreen()+c.getRed())/3;
				if(avg>flag){
					re[i][j]=0;
				}else{
					re[i][j]=1;
				}
				
			}
		}
		return re;
	}
	
	 /**
     * 缩放Image，此方法返回源图像按长宽缩放后的图像
     */
	private BufferedImage scaleByPercentage(BufferedImage inputImage, int w,int h) {
		// 获取原始图像透明度类型
		int type = inputImage.getColorModel().getTransparency();
		// 开启抗锯齿
		//RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_ANTIALIAS_ON);
		// 使用高质量压缩
		//renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_RENDER_QUALITY);
		BufferedImage img = new BufferedImage(w, h, type);
		Graphics2D graphics2d = img.createGraphics();
		//graphics2d.setRenderingHints(renderingHints);
		graphics2d.drawImage(inputImage, 0, 0, w, h, 0, 0, inputImage.getWidth(), inputImage.getHeight(), null);
		graphics2d.dispose();
		return img;
	}
	/*
	 * 切除图片周围空白的区域
	 * */
	public double[][] cutImage(double[][] data){
		int wid=data.length;
		int hei=data[0].length;
		int[] g={0,0,wid,hei};
		for(int i=0;i<wid;i++){
			boolean isAll0=true;
			for(int j=0;j<hei;j++){
				if(data[i][j]!=0){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[0]=i+1;
			}else{break;}
		}
		for(int i=0;i<hei;i++){
			boolean isAll0=true;
			for(int j=0;j<wid;j++){
				if(data[j][i]!=0){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[1]=i+1;
			}else{break;}
		}
		for(int i=wid-1;i>=0;i--){
			boolean isAll0=true;
			for(int j=0;j<hei;j++){
				if(data[i][j]!=0){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[2]=i-g[0];
			}else{break;}
		}
		for(int i=hei-1;i>=0;i--){
			boolean isAll0=true;
			for(int j=0;j<wid;j++){
				if(data[j][i]!=0){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[3]=i-g[1];
			}else{break;}
		}
		if(g[2]==wid){
			g[2]-=g[0];
		}
		if(g[3]==hei){
			g[3]-=g[1];
		}
		return cutArray(data,g[0], g[0]+g[2],g[1],g[1]+g[3]);
	}
	/*
	 * 切除图片周围空白的区域
	 * */
	private BufferedImage cutImage(BufferedImage img,double[][] data){
		int wid=data.length;
		int hei=data[0].length;
		int[] g={0,0,wid,hei};
		for(int i=0;i<wid;i++){
			boolean isAll0=true;
			for(int j=0;j<hei;j++){
				if(data[i][j]==1){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[0]=i+1;
			}else{break;}
		}
		for(int i=0;i<hei;i++){
			boolean isAll0=true;
			for(int j=0;j<wid;j++){
				if(data[j][i]==1){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[1]=i+1;
			}else{break;}
		}
		for(int i=wid-1;i>=0;i--){
			boolean isAll0=true;
			for(int j=0;j<hei;j++){
				if(data[i][j]==1){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[2]=i-g[0];
			}else{break;}
		}
		for(int i=hei-1;i>=0;i--){
			boolean isAll0=true;
			for(int j=0;j<wid;j++){
				if(data[j][i]==1){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[3]=i-g[1];
			}else{break;}
		}
		if(g[2]==wid){
			g[2]-=g[0];
		}
		if(g[3]==hei){
			g[3]-=g[1];
		}
		return img.getSubimage(g[1], g[0], g[3], g[2]);
	}
	
	/*
	 * 只切除横向空白的区域
	 * */
	private BufferedImage cutImageX(BufferedImage img,double[][] data){
		int wid=img.getWidth();
		int hei=img.getHeight();
		int[] g={0,0,wid,hei};
		for(int i=0;i<wid;i++){
			boolean isAll0=true;
			for(int j=0;j<hei;j++){
				if(data[i][j]==1){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[0]=i+1;
			}else{break;}
		}
		
		for(int i=wid-1;i>=0;i--){
			boolean isAll0=true;
			for(int j=0;j<hei;j++){
				if(data[i][j]==1){
					isAll0=false;break;
				}
			}
			if(isAll0){
				g[2]=i-g[0];
			}else{break;}
		}
		if(g[2]==wid){
			g[2]-=g[0];
		}
		return img.getSubimage(g[0], 0, g[2],hei);
	}
	
	/*
	 * 切割矩阵
	 * */
	private double[][] cutArray(double[][] arr,int xB,int xE,int yB,int yE){
		int w=xE-xB,h=yE-yB;
		double[][] re=new double[w][h];
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				re[i][j]=arr[i+xB][j+yB];
			}
		}
		return re;
	}
	public BufferedImage arrToImg(double[][] arr){
		int h=arr.length,w=arr[0].length;
		BufferedImage img=new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				if(arr[i][j]!=0){
					img.setRGB(j, i, BLACK.getRGB());
				}else{
					img.setRGB(j, i, WHITE.getRGB());
				}
			}
		}
		return img;
	}
	private String formatFileName(String data){
		if("<".equals(data)){
			return "&lt";
		}else if(">".equals(data)){
			return "&gt";
		}else if("/".equals(data)){
			return "&x";
		}else if("\\".equals(data)){
			return "&fx";
		}else if("\"".equals(data)){
			return "&quot";
		}else if("*".equals(data)){
			return "&all";
		}else if("?".equals(data)){
			return "&laquo";
		}else if("|".equals(data)){
			return "&brvbar";
		}else if(":".equals(data)){
			return "&mh";
		}else if(".".equals(data)){
			return "&d";
		}else{
			return data;
		}
	}
	/*
	private <T> T[] arrLink(T[] first, T[] second) {  
		T[] result = Arrays.copyOf(first, first.length + second.length);  
		System.arraycopy(second, 0, result, first.length, second.length);  
		return result;  
	} */
}
