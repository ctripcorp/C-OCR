package code;

import entity.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import util.RejectUtil;


public class RejectBiz {

    /**
     * 获取拒识结果
     *
     * @param origin 原图
     * @return 拒识结果 1 表示拒识 0 不拒识
     */
    public static int getRejectFlag(Mat origin) throws Exception {

        int rejectFlag =0;
        int h = origin.rows(),w = origin.cols();
        int N = 256;

        //region rgb2gray
        Mat gray =RejectUtil.getGray(origin,h,w);
        //endregion
        // log edge
        LogParameter logParameter = RejectUtil.logEdge(origin);
        double log_white_sum = logParameter.getLog_white_sum();
//        double log_radio = logParameter.getLog_radio();
        //region laplace and hist
        LaplaceEntity lap = RejectUtil.lapalace(gray,N,h,w);
        int [] hist = lap.getHist();
        Mat laplace = lap.getImg();
//        //endregion
        CoreParameterEntity cp = getCoreParamter(gray,laplace,hist,N,w,h);
        //region judge
        Result res = getResult(log_white_sum, cp);
        if(res!=null){
            rejectFlag = res.getOutResult();
        }
        return rejectFlag;
        //endregion
    }

    /**
     * 获取二值化图以及拉普拉斯变化图的基本信息,如均值、方差等
     */
    private static CoreParameterEntity getCoreParamter(Mat gray,Mat laplace,int[] hist,int N,int w,int h){
        double FLT_EPSILON = 1.19209290E-07;
        //region calc
        MatOfDouble l_avg = new MatOfDouble();
        MatOfDouble l_std = new MatOfDouble();
        Core.meanStdDev(laplace,l_avg,l_std);
        MatOfDouble avg = new MatOfDouble();
        MatOfDouble std = new MatOfDouble();
        Core.meanStdDev(gray,avg,std);
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(gray);
//        double e = (minMaxLocResult.minVal+1)/(255-minMaxLocResult.maxVal+minMaxLocResult.minVal+1);
        int hist_peak = 0;
        int maxHist = 0;
        double en = 0;
        int brightSum=0;
        int grayMaxSum = hist[N-1];
        double brightSum_radio= 0.0;
        for(int i = 0;i < N;i++){
            if(hist[i] != 0){
                double p = hist[i]/(double)gray.total();
                en += p*Math.log(1/p)/Math.log(2);
            }
            if(hist[i] > maxHist){
                maxHist = hist[i];
                hist_peak = i;
            }
            if (i>250){
                brightSum+=hist[i];
            }
        }

        //endregion
        //region binary otsu
        double mu = 0, scale = 1./(w*h);
        for(int i = 0; i < N; i++ )
            mu += i*(double)hist[i];
        mu *= scale;

        double mu1 = 0, q1 = 0;
        double max_sigma = 0, max_val = 0;

        for(int i = 0; i < N; i++ ) {
            double p_i, q2, mu2, sigma;

            p_i = hist[i]*scale;
            mu1 *= q1;
            q1 += p_i;
            q2 = 1. - q1;

            if( Math.min(q1,q2) < FLT_EPSILON || Math.max(q1,q2) > 1. - FLT_EPSILON )
                continue;

            mu1 = (mu1 + i*p_i)/q1;
            mu2 = (mu - q1*mu1)/q2;
            sigma = q1*q2*(mu1 - mu2)*(mu1 - mu2);
            if( sigma > max_sigma ) {
                max_sigma = sigma;
                max_val = i;
            }
        }
        double bw_radio = 0;
        for(int i = (int)max_val + 1;i < N;i++){
            bw_radio += hist[i];
        }
        double pixCount = h*w;
        double bw_black_sum = pixCount-bw_radio;
        bw_radio /= pixCount;
        brightSum_radio = brightSum/pixCount;
        //endregion
        //region brenner and energy
        double d_energy = 0,brenner = 0;
        for(int i = 0;i < gray.rows()-2;i++){
            for(int j = 0;j < gray.cols() - 2;j++){
                double temp = (gray.get(i+2,j)[0]-gray.get(i,j)[0])/255;
                brenner += Math.pow(temp,2);
            }
        }
        for(int i = 1;i < gray.rows();i++) {
            for (int j = 0; j < gray.cols() - 1; j++) {
                double tempOne = (gray.get(i,j)[0]-gray.get(i-1,j)[0])/255;
                double tempTwo = (gray.get(i,j)[0]-gray.get(i,j+1)[0])/255;
                d_energy += Math.pow(tempOne,2) + Math.pow(tempTwo,2);
            }
        }
        double r_std = std.get(0,0)[0];
        double r_l_std = l_std.get(0,0)[0];
        double g_max = minMaxLocResult.maxVal;
        double r_avg = avg.get(0,0)[0];
        //endregion
        CoreParameterEntity cp = new CoreParameterEntity(en, r_std, r_l_std, bw_radio, hist_peak, g_max, brenner, d_energy, r_avg, pixCount,  grayMaxSum, brightSum, brightSum_radio);
        return cp;

    }

    /**
     * 根据图像特征获取不同场景下拒识结果
     */
    private static Result getResult(double log_white_sum, CoreParameterEntity cp){

        double en = cp.getEn(), std = cp.getStd(), l_std = cp.getL_std(), bw_radio = cp.getBw_radio();
        double hist_peak = cp.getHist_peak(), g_max = cp.getG_max(), d_brenner = cp.getD_brenner();
        double d_energy = cp.getD_energy(), avg = cp.getAvg(),  pixCount = cp.getPixCount(), brightSum_radio = cp.getBrightSum_radio();
        int grayMaxSum = cp.getGrayMaxSum(), brightSum = cp.getBrightSum();
        //region judge param
        Result re = new Result();
        double p_sum = l_std+d_brenner+d_energy;
        double p_le_sum = l_std+d_energy;

        double unit_log_white_sum = (40000/pixCount)*log_white_sum;

        // bw_radio<0.45
        boolean b_one = (bw_radio<0.45 && l_std<21) || (bw_radio<0.718  && l_std<35  && en<7.38 ) || (l_std>35 && d_energy>568 && en>6.28 && avg<215 && bw_radio<0.860 );
        // bw_radio<0.701
        boolean  b_two = bw_radio<0.701 && l_std<14 && en<7.36 && std<52 && d_energy<130 && d_brenner<240;
        boolean  b_three = bw_radio<0.701 && l_std>40  && std>32 && d_energy>1000 && d_brenner<2000 && avg<200;
        boolean  b_four = bw_radio<0.68 && bw_radio >0.478 && avg<152 && (l_std<21 ||l_std>30) && std>30 && en> 6.6 && hist_peak<142 && log_white_sum<1358;
        // bw_radio<0.802
        boolean  b_five = bw_radio<0.808 && bw_radio>0.7 && (l_std<17 || log_white_sum <80) && std<40  && en<6.9 && d_brenner<500 &&  d_energy<460 &&(log_white_sum< 1080 || std>39);
        boolean  b_six = bw_radio<0.802 && bw_radio>0.792 && l_std<7.8 && std<31 && avg>108 && en<6.5 && d_brenner<86 && d_energy<68 && log_white_sum<668;

        // bw_radio>0.9216
        boolean b_seven = bw_radio>0.9270 && l_std<2.65 && d_energy <15 && d_brenner<20 && en <4.5 && avg >200 && log_white_sum<100;

        boolean b_eight = bw_radio>0.9381  && log_white_sum<400  && l_std<6.0 && avg>170 && en<5.6 && d_brenner<31 && d_energy<22 && l_std<6.8;

        // bw_radio>0.90 bw_radio<0.9217
        boolean b_nine = bw_radio>0.90 && bw_radio<0.9217 && log_white_sum<268 && l_std>2.0 && l_std<5.0 && (en>4.68 || en<4.41) && std<28  && avg>128  && d_brenner<52  && d_energy<28;

        boolean b_ten =  log_white_sum>7680 && avg>170 && l_std<24 && en<7.1 && std>35  &&  d_brenner<1300 && d_energy<900;

        boolean b_eleven = bw_radio>0.90 && bw_radio<0.9217 && ((log_white_sum<678 && l_std>4.0 && l_std<4.5 && en<5.28 && std<22 && avg>190  && d_brenner<100 && d_energy<50)||(log_white_sum<886 && l_std>6.3 && l_std<6.6 && en<5.08 && std<22 && avg>210  && d_brenner<108 && d_energy<50));

        // bw_radio>0.801 && bw_radio<0.831
        boolean b_twelve = bw_radio>0.808 && bw_radio<0.839 && l_std<7.83 && std<39 && std >25 && en <6.8 && d_energy<61 && avg>130 && (log_white_sum<1200 || l_std<5.1) && d_energy>46.69 && hist_peak>136;

        boolean b_thirteen = bw_radio>0.801 && bw_radio<0.831 &&  std<41.2 && en <6.8  && avg>94 && d_energy<38 && d_energy>26.8 &&
                ((d_brenner<45 && l_std <5.5 && log_white_sum<420) || (std>33  && d_brenner>80 && l_std<5.5 && log_white_sum<788) || ( d_brenner>80 && en<5.2 && l_std<6.0 && d_energy<33) );

        // 有一定误差
        boolean b_fourteen = bw_radio>0.80383 && bw_radio<0.831 && l_std<7.1 &&  std<41.2 && en <6.8  && en>4.2 &&  avg>94 && d_energy<26.6 && log_white_sum<520 && ((log_white_sum==0 && p_sum<60) || (std>17 && bw_radio>0.806 && avg>140 && avg<212) );

        // bw_radio>0.83 && bw_radio<0.9
        boolean b_fiveteen = bw_radio>0.83 && bw_radio<0.9 && l_std<3.10 && en<6.0 && d_brenner<19 && ((hist_peak<126 && d_energy<10 && d_brenner<15 && d_energy>3.9 && l_std<2.9) || hist_peak>126);

        // l_std>3.216 && <4.99  bw_radio>0.83 && && bw_radio<0.896 && l_std>3.216 && l_std<4.99   && std<35 && en >3.76  && log_white_sum<720  && d_brenner<67 && d_energy<44.839 && p_sum<111

        boolean b_sixteen = bw_radio>0.83 && bw_radio<0.90 && l_std>4.6 && l_std<4.99 && std<35 &&  d_energy<44.839  && d_brenner<67 && avg <195 && avg>112 && en<5.8 && en >3.76 && log_white_sum<720 && p_sum<111;

        boolean b_seventeen =  bw_radio<0.896 && l_std<4.99 && d_energy<32 && avg>200  && std<28 &&  en >3.76 && en <6.18  && log_white_sum<688  && d_brenner<66 && p_sum<95;


        boolean b_eighteen  = l_std>3.216 && l_std<4.56 && bw_radio>0.83 &&  bw_radio<0.896  && std<35 && en >3.76  && log_white_sum<288  && d_brenner<45.40 && d_brenner>27.8 && d_energy<26 && p_sum<111 && avg<198 && avg>112;


        boolean b_nineteen = l_std>3.216 && l_std<4.41 && bw_radio>0.83  && bw_radio<0.896   && std<35 && en >3.76 && en < 6.0  && log_white_sum<500  && d_brenner<26.86 && d_energy<26 && p_sum<50.8 && avg<198 && (avg>112 || avg< 82);

        boolean b_twenty =  bw_radio>0.83 && bw_radio<0.9 && l_std<2.8 && std<28.78 &&  d_energy<15 && d_brenner<23 && en<6.10 && en>5.18 && avg>176 && log_white_sum<=0;

        boolean b_twentyone =  bw_radio>0.83 && bw_radio<0.896 && (l_std<4.28 && l_std>3.9) &&  (std<25 || std>46.68) && d_brenner<73 &&  d_energy<36.98 && en<5.68 &&
                avg>190 && log_white_sum<828;

        boolean b_twentytwo =  bw_radio>0.835 && bw_radio<0.862 && (l_std<5.30 && l_std>4.96) &&  std<33.18 && std>24 && d_brenner<116.98  && en<6.18 && avg>180 &&
                log_white_sum<1218 && log_white_sum>698 && hist_peak>190 && d_brenner<116.98 &&  d_energy<50;

        // l_std>4.99  std<35.68
        // std<21.88
        boolean b_twentythree = bw_radio>0.83 && bw_radio<0.90 && std< 21.88 && avg>195 && l_std>5.0 && l_std<6.0 && en< 6.28 && d_energy<35 && d_brenner<42 && log_white_sum<500;

        boolean b_twentyfour = bw_radio>0.83 && bw_radio<0.90 && std< 21.88  && l_std<16.68 && en< 6.28 && d_energy<60 && d_brenner<58 && log_white_sum<228 && avg<178;

        //  std>21.88 && std<26.31
        boolean b_twentyfive = bw_radio>0.83 && bw_radio<0.90 && std>22.68 && std<26.18  && avg>208 && en<6.58 && d_brenner <208 && d_energy<120 && log_white_sum<1628 && l_std<12;

        boolean b_twentysix =  bw_radio>0.840 && bw_radio<0.888 && std>23  && std<26.38 && d_energy<58 && d_brenner<112 &&  d_brenner>19 && avg<201.20 && avg>170 && ((l_std<7.6 && l_std>5.88) || (l_std<5.4 && l_std>5.26))  && log_white_sum<1158 && log_white_sum>260 && en>5.05 && en<6.33 && hist_peak>180 ;
        // std>26 && std<30
        boolean b_twentyseven = bw_radio>0.83 && bw_radio<0.90 && std>26 && std<30  && avg>180 && g_max>=255 && en>6.28 && en>5.86 && d_brenner <258 && d_energy<106 && log_white_sum<2200 && l_std<9.8;

        boolean b_twentyeight = bw_radio>0.83 && bw_radio<0.90 && std>26 && std<30  && l_std<5.58 && avg<136 && en<6.28  && d_brenner <78 && d_energy<35.31 && log_white_sum<600;

        boolean b_twentynine = bw_radio>0.83 && bw_radio<0.90 && std>26 && std<30  && l_std<8.1 && l_std>7.858 && avg>182  && en<5.85  && d_brenner <25.88 && d_energy<58 && log_white_sum<1200;

        // std>30 && std<35.88

        boolean b_thirty = bw_radio>0.845 && bw_radio<0.899 && std>30 && std<35.88 && l_std<9.58 && l_std>5.80 && d_energy<40 && avg>200 && en>5.08 && en<6.28  && log_white_sum<928 && p_sum<90 && p_le_sum<50;

        boolean b_thirtyone = bw_radio>0.845 && bw_radio<0.899 && std>30 && std<35.88 && l_std<9.58 && l_std>6.30 && d_energy<110.08 && avg>202 && avg<205.8 && en>5.56 && bw_radio>0.845 && log_white_sum<1988 &&
                hist_peak<225 ;

        boolean b_thirtytwo = bw_radio>0.845 && bw_radio<0.899 && std>30 && std<35.88 && l_std<9.58 && l_std>6.58 && d_energy<60 && avg>168 && en<6.18 && ((pixCount<28980 && d_energy<42 && d_brenner<30) || pixCount<26800) && log_white_sum<1268;

        boolean b_thirtythree=  bw_radio>0.86 && bw_radio<0.88 && (l_std<15.18 && l_std>10) &&  std<25 && d_brenner<346  && en<6.32 && en>6.18 && avg<158 &&
                log_white_sum<496 && log_white_sum>268 && hist_peak<168;

        boolean b_thirtyfour=  bw_radio>0.86 && bw_radio<0.88 && (l_std<15.18 && l_std>10) &&  std<25 && d_brenner<346  && en<6.32 && en>6.18 && avg<158 &&
                log_white_sum<496 && log_white_sum>268 && hist_peak<168;

        boolean b_thirtyfive=  bw_radio>0.878 && bw_radio<0.888 && (l_std<8.39 && l_std>7.8) &&  std<25 && d_brenner<135  && en<5.58 && en>5.28 && avg>193 &&
                log_white_sum<1150 && log_white_sum>1000 && hist_peak>203;

        boolean b_thirtysix =  bw_radio>0.83 && bw_radio<0.896 && l_std>3.216 && l_std<4.99 && d_energy<33.9 && avg>203  && std<30 &&  en >3.76 && en <4.9  && log_white_sum<720 && d_brenner<86 && p_sum<120;

        boolean b_thirtyseven= avg>216 && l_std<18.6 && d_energy<178  && std<35 && en <6.8  && log_white_sum>700 && log_white_sum<1800 && d_brenner<138 && p_sum<350 && hist_peak>253 && g_max>=255;

        boolean b_thirtyeight = avg>178 && hist_peak>170 && g_max>250 && l_std>21 && std>29 && bw_radio>0.89 && unit_log_white_sum>4200;

        boolean b_thirtynine = l_std<4.08 &&  d_energy<13.58 && d_brenner<31 && en<5.58 && (avg>210 || avg<128 || l_std<3.18 ) && en<5.5 && log_white_sum<158 && (bw_radio>0.90|| bw_radio<0.85);

        boolean b_fourty = unit_log_white_sum>3600 && unit_log_white_sum<3650 && avg>192 && avg<206 && std>31 && en<6.58 && en>6.38 && l_std<21.08 && hist_peak>189 && bw_radio>0.88;

        boolean b_fourtyone = log_white_sum<618 && log_white_sum>600 && avg<176 && avg>150 && std>18 && std<20 && en<5.58 && en>5.18 && l_std<6.98 && hist_peak>173 && bw_radio>0.888;

        boolean b_fourtytwo = log_white_sum<640 && log_white_sum>360 && (avg<118 || avg>193) && std<24 && std>16 && en<5.18  && l_std<6.80 && l_std>4.5 && (hist_peak>200 ||hist_peak<128)  && p_le_sum<29 && p_sum<72 && (bw_radio>0.90 || bw_radio<0.861);

        boolean b_fourtythree = l_std<4.0  && std<31 && d_energy<26.88 && en<6.20 && avg<183 && d_brenner<68 && p_sum<108 && (log_white_sum<150 || (log_white_sum>500 && log_white_sum<800)) && bw_radio>0.8028 && bw_radio<0.8708;

        boolean b_fourtyfour = log_white_sum<180 && l_std>4.3 && l_std<5.8 && ( (d_energy<35.68 && d_brenner<45.68) || std>45) && en<7.08 && avg<205 && hist_peak>178;

        boolean b_fourtyfive = log_white_sum<400 && log_white_sum>358 && l_std>10.8 && l_std<15.68 && d_energy<228 && d_brenner<258 && en<7.08 && avg<188 && avg>160 && hist_peak>178 && hist_peak<208 && (std>43 || d_brenner<20);

        boolean b_fourtysix = bw_radio<0.218 && l_std<20 && l_std>10 && std>35 && en<6.98 && g_max>254 && avg<180 && hist_peak<188;

        boolean b_fourtyseven = l_std>5.50 && l_std<5.80 && en<5.9 && en>5.6 && std<31 && std>24 && d_energy<33.28 && d_energy>28.68 && log_white_sum>496 && log_white_sum<688 && bw_radio>0.83 && bw_radio<0.89;

        boolean b_fourtyeight = (std>72 || l_std<1.88 || d_energy<3.2 || log_white_sum<=10) && en<6.98 && avg<208 && l_std<21.68  && log_white_sum<1288 && (bw_radio<0.870 || log_white_sum<5 );

        boolean b_fourtynine = (hist_peak>250 && g_max>253) && ( (avg>218 && l_std<10.68 && log_white_sum<518 ) || (bw_radio<0.80 && std>42 && avg>188 && en<5.28 && log_white_sum<1368 ));

        boolean b_fivety = (l_std<4.68 ||(l_std>6.0 && l_std<6.5)) && avg>175 && avg<203 && en>5.49 && en<6.08 && d_energy<32.18 && d_brenner<95 && std>22 && std<30 && log_white_sum>688 && log_white_sum<898 && bw_radio>0.83 && bw_radio<0.86 && hist_peak>188 && hist_peak<218;

        boolean b_fivetyone = avg<153 && std>23 && std<28.28 && en<6.38 && en>5.68 && d_brenner<130 && d_brenner>80 && d_energy<70 && l_std<8 && l_std>5.68 && hist_peak>178 && log_white_sum<1288 && bw_radio>0.88 && bw_radio<0.91 && (log_white_sum<888 || (l_std<5.0 && log_white_sum<1288 && avg<170));

        boolean b_fivetytwo = (l_std<5.18 ||(l_std<5.408 && avg>215 && hist_peak>220)) &&  std<30 && std>16.08 && avg>139.68 && en<6.31 && bw_radio<0.908 && d_energy<50 && d_brenner<138 &&  bw_radio<0.9016 && (log_white_sum<808 || (log_white_sum<1268 && bw_radio<0.858) );

        boolean b_fivetythree = log_white_sum>150 && log_white_sum<528 && en<5.88 && std<28.68 && avg<188 &&  d_brenner<68 && d_energy<40 && ((l_std>5.68 && l_std<6.98 ) || (l_std<15 && hist_peak>188 && g_max>205 && avg>178)) && bw_radio<0.88;
        // Exposure detection
        boolean b_fivetyfour  = (brightSum>72 || ((brightSum>30 && grayMaxSum>3 && hist_peak>208 && avg>200) || (brightSum>60 && grayMaxSum>1)))  && g_max>252 && avg>180 && l_std>6.08 && std>20 && hist_peak>180 && (brightSum_radio>0.0020 || hist_peak>212 || bw_radio<0.80) && (en<6.88 || l_std>33) && std<39.58 && log_white_sum<2058;

        boolean b_fivetyfive = (brightSum>1258 || (grayMaxSum>50 && brightSum>258) || brightSum_radio> 0.029) && (l_std>5.0 || l_std<4.5) && en>5.0 && log_white_sum<3081;

        boolean b_fivetysix = (l_std<4.68 || (log_white_sum<88 && l_std<15))&& en<5.88 && std< 27.88 && avg>166 && avg<190 && hist_peak>168 && log_white_sum<288 && (d_energy<29.88 || log_white_sum<88) && d_brenner<30.58 ;

        boolean b_fivetyseven = (log_white_sum<128 || (log_white_sum<278 && std<26.98 )) && bw_radio< 0.872 && en>5.88 && std>21 && avg>140 && avg<212 && d_brenner <78 && d_energy>78 && l_std>10 && hist_peak>172 && g_max>218;

        boolean b_fivetyeight = ((log_white_sum<288 && bw_radio<0.85)|| log_white_sum<180)&& (avg>190 || (avg>183 && bw_radio<0.839)) && hist_peak>190 && g_max>241 && std<41.58 && en<7.08 && en>5.39 && (l_std<15.18 || l_std>26.18) && ((d_brenner<92 && d_energy<100) || l_std>26.08);

        boolean b_fivetynine = (l_std<4.30 && l_std>3.99 && avg>196  && avg<210 && en<5.68 && d_brenner<72 && d_energy<32.58 && hist_peak>190 && g_max>217 && log_white_sum<1000 && log_white_sum>468) || (bw_radio>0.918 && l_std<8.58 && l_std>6.58 && en<6.58 && en>5.58 && std<26.68 && avg>190 && hist_peak>220 && g_max>230 && d_energy<72 && d_brenner<150);

        boolean b_sixty = (g_max>239 && hist_peak>231 && (l_std<11.98 || (brightSum>30 && l_std<15.18) ) && std<32.58 && avg>198 && en<6.68 && en>5.08 && unit_log_white_sum<1408 && d_energy>22.08) || (g_max>249 && hist_peak>206 && l_std>18.58 && avg>198  && log_white_sum<1218 && log_white_sum>600 && d_brenner<72 && d_energy<318 && bw_radio<0.896 && en<6.88 && en>4.60);

        boolean b_sixtyone = (log_white_sum<168 && (std<25 || std>36.28) && avg<208 && en>5.58 && d_brenner<80 && (d_energy<68 || bw_radio<0.828) && ((l_std<11.98 && bw_radio<0.838 ) || l_std<4.18)) || ( avg<180 && std>30.18 && ((l_std>7.98 && bw_radio<0.859) || l_std>9.78) && en>6.18 && g_max>248 && hist_peak<186 && (bw_radio<0.896 || brightSum>72) && log_white_sum>1568 && p_sum<1068 && p_le_sum<568 )
                || (l_std<5.98 &&  hist_peak>212 && (g_max>247 || (l_std<3.98 && g_max>225)) && d_energy<39.98 && d_brenner<72 && en<6.38  && std<29.28 && avg>195 && log_white_sum<1028 && en>5.18);

        boolean b_sixtytwo = bw_radio<0.85 && (l_std<10.08 || brightSum>200) && g_max>230 && hist_peak>165 && en >6.08 && avg>158 && std>21.18 && std<42.68 && ((d_energy<158 && d_brenner<188 && bw_radio<0.818) ||( d_brenner<78 && d_energy<68));

        boolean b_sixtyThree = l_std<7.9 && hist_peak>215 && g_max>246 && d_energy<72 && d_brenner<150 && en<6.9 && en>5.8 && std>24 && avg>198 && brightSum>100 && log_white_sum<788;

        boolean b_sixtyFour = (log_white_sum<168 &&  avg<150 && l_std<4.9  && d_energy<41 && d_brenner<58  && en<6.88) || (brightSum>128 && avg>158 && avg<206 && en>5.68 && l_std>11.28 && l_std<17.58 && g_max>251 && hist_peak>180 && d_energy<208 && d_brenner<258 && std>32) ;

        boolean b_sixtyFive = en>4.26 && l_std<6.08 && bw_radio>0.863 && en<6.08 && ((log_white_sum<688 && d_brenner<78) || (log_white_sum<1158 && l_std<5.28 && d_brenner<108 && en<5.18)) && d_energy<40 && en<6.18 && std<29.68 && (avg>196 || avg<168) && ((hist_peak>198 && g_max>220) || (hist_peak<178 && log_white_sum<208));

        boolean b_sixtySix = l_std>4.419 && avg>98  && (d_energy<36.28 || d_brenner<36.58) && d_brenner<72 && en<6.18 && avg<196 && std<31.58 && (l_std<8.58 || (l_std<11.28 && d_brenner<23.68)) && (log_white_sum<688 || (log_white_sum<988 && d_brenner<18)) && hist_peak<218;

        boolean b_sixtySeven = l_std<7.18 && l_std>6.39 && en<5.78 && avg<190 && avg>121.58 && std<28 &&  d_energy<68 && ( (d_brenner<88 && log_white_sum<768) || (d_brenner<168 && en<5.08 && std<23.18 && log_white_sum<1278 )) && hist_peak<208 ;

        boolean b_sixtyEight = l_std<6.58 && d_energy<52.68 && d_brenner<35.68 && std<32.58 && avg<180 && (avg>150 || avg<126) && en<5.93 && hist_peak<198 && log_white_sum<1018 && bw_radio< 0.88;

        boolean b_sixtyNine = brightSum>498 && g_max>250 && avg>180 && std>28.18 && en>6.18 && hist_peak>180 && d_brenner<368 && d_brenner>158 && d_energy>188 && d_energy<398 && brightSum_radio>0.01 && (bw_radio>0.878 || bw_radio<0.839);

        //endregion
        int result = 0;
        int outResult = 0;
        // region get result
        if (b_one)
            result =1;
        else if (b_fivetyfive){
            result =55;
        }
        else if( b_two )
            result =2;
        else if( b_three )
            result =3;
        else if( b_four )
            result =4;
        else if( b_five )
            result =5;
        else if( b_six )
            result =6;
        else if( b_seven)
            result =7;
        else if( b_eight)
            result =8;
        else if( b_nine)
            result =9;
        else if ( b_ten )
            result =10;
        else if ( b_eleven )
            result =11;
        else if ( b_twelve )
            result =12;
        else if ( b_thirteen )
            result =13;
        else if ( b_fourteen )
            result =14;
        else if( b_fiveteen )
            result = 15;
        else if( b_sixteen )
            result = 16;
        else if ( b_seventeen )
            result = 17;
        else if ( b_eighteen )
            result = 18;
        else if( b_nineteen )
            result = 19;
        else if( b_twenty )
            result = 20;
        else if( b_twentyone)
            result =21;
        else if( b_twentytwo)
            result =22;
        else if( b_twentythree)
            result =23;
        else if( b_twentyfour)
            result =24;
        else if( b_twentyfive)
            result =25;
        else if (b_twentysix){
            result = 26;
        }
        else if(b_twentyseven){
            result = 27;
        }
        else if (b_twentyeight){
            result = 28;
        }
        else if ( b_twentynine )
            result =29;
        else if ( b_thirty )
            result =30;
        else if ( b_thirtyone )
            result =31;
        else if ( b_thirtytwo )
            result =32;
        else if ( b_thirtythree )
            result =33;
        else if ( b_thirtyfour )
            result =34;
        else if ( b_thirtyfive )
            result =35;
        else if ( b_thirtysix )
            result =36;
        else if(b_thirtyseven){
            result = 37;
        }
        else if(b_thirtyeight){
            result = 38;
        }
        else if(b_thirtynine){
            result = 39;
        }
        else if(b_fourty){
            result = 40;
        }
        else if(b_fourtyone){
            result = 41;
        } else if(b_fourtytwo){
            result = 42;
        }
        else if(b_fourtythree){
            result = 43;
        }
        else if(b_fourtyfour){
            result = 44;
        }
        else if(b_fourtyfive){
            result = 45;
        }
        else if(b_fourtysix){
            result = 46;
        } else if(b_fourtyseven){
            result = 47;
        } else if(b_fourtyeight){
            result = 48;
        } else if(b_fourtynine){
            result = 49;
        } else if(b_fivety){
            result = 50;
        } else if( b_fivetyone){
            result =51;
        } else if (b_fivetytwo){
            result =52;
        } else if (b_fivetythree){
            result =53;
        } else if (b_fivetyfour){
            result =54;
        } else if (b_fivetysix){
            result =56;
        } else if(b_fivetyseven){
            result =57;
        } else if(b_fivetyeight){
            result =58;
        } else if(b_fivetynine){
            result =59;
        } else if (b_sixty){
            result = 60;
        } else if (b_sixtyone){
            result = 61;
        } else if (b_sixtytwo){
            result = 62;
        } else if (b_sixtyThree){
            result = 63;
        } else if (b_sixtyFour){
            result =64;
        } else if (b_sixtyFive){
            result = 65;
        } else if (b_sixtySix){
            result = 66;
        } else if (b_sixtySeven){
            result =67;
        } else if (b_sixtyEight){
            result =68;
        } else if (b_sixtyNine){
            result =69;
        }
        // endregion
        if ( result>0  )
            outResult = 1;
        re.setOutResult(outResult);
        re.setResult(result);
        return re;
    }


}
