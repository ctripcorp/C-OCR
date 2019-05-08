package logistic;

import java.util.List;
import java.util.Map;


public class LogisticModel {
	private double[] sts=null;//参数Θ
	private double a=0.1;//学习速率
	private List<Map<String,Object>> list=null;
	
	public LogisticModel(List<Map<String,Object>> list){
		this.list=list;
		Map<String,Object> map=list.get(0);
		double[] x=(double[])map.get("x");
		sts=new double[x.length];
	}
	public LogisticModel(){
	}

	public double function(double[] xs){//函数模型
		double re=0f;
		for(int i=0;i<xs.length;i++){
			re+=xs[i]*sts[i];
		}
		return 1/(Math.pow(Math.E, -re)+1);//logistic函数
	}
	/**
	 * @author Administrator
	 * @Description 使用梯度下降算法进行函数参数更新（学习）
	 * @date 2018年7月16日 下午2:28:09
	 */
	public void update(){//模型参数的更新
		double[] stss=new double[sts.length];//新的模型参数，此处单独用数组来装而不是直接对该参数赋值，是为了不影响下一个参数的学习，保证每个参数对应的都是同一个函数
		int len=list.size();
		for(int i=0;i<stss.length;i++){//遍历每一个参数
			double sum=0f;
			for(Map<String,Object> map:list){
				double[] xs=(double[])map.get("x");
				double y=(double)map.get("y");
				sum+=(function(xs)-y)*xs[i];
			}
			//System.out.println("js---:"+a*(1.0f/len)*sum);
			stss[i]=sts[i]-a*(1.0f/len)*sum;//更新该参数
		}
		sts=stss;//统一更新参数
	}
	public double dj(){//代价函数
		double sum=0f;
		for(Map<String,Object> map:list){
			double[] xs=(double[])map.get("x");
			double y=(double)map.get("y");
			sum+=y*Math.log(function(xs))+(1-y)*Math.log(1-function(xs));
		}
		return -(1.0f/list.size())*sum;
	}
	public void go(){
		int sum=0;//参数迭代次数
		int count=0;
		while(true){
			double oldDj=dj();//迭代前损失
			sum++;
			if(sum>=10000){//迭代次数不超过10000
				break;
			}
			update();//迭代
			double newDj=dj();//迭代后损失
			if(Math.abs(newDj-oldDj)<0.00001){//两次误差
				count++;
				if(count>10){//如果损失小于0.00001连续10次,则认为已经拟合
					break;
				}
			}else{count=0;}
		}
//		for(int j=0;j<sts.length;j++){
//			System.out.println("st"+j+":"+sts[j]+"  ");
//		}
		System.out.println("dj:"+dj()+"   sum:"+sum);

	}
	public double[] getSts() {
		return sts;
	}
	public void setSts(double[] sts) {
		this.sts = sts;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	
}
