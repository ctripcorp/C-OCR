package entity;

public class CoreParameterEntity {

    // 熵
    private  double en;
    // 二值化均方差
    private  double std;
    // laplace 均方差
    private double l_std;
    // 二值化占比
    private double bw_radio;
    // 灰度峰值
    private double hist_peak;

    private double g_max;

    private double d_brenner;

    double d_energy;
    // 均值
    private double avg;
    // 像素总和
    private double pixCount;
    // 灰度像素总和
    private int grayMaxSum;
    // 亮度像素总和
    private int brightSum;
    // 亮度像素总和占比
    private double brightSum_radio;

    public CoreParameterEntity(double en, double std, double l_std, double bw_radio, double hist_peak, double g_max, double d_brenner, double d_energy, double avg, double pixCount, int grayMaxSum, int brightSum, double brightSum_radio) {
        this.en = en;
        this.std = std;
        this.l_std = l_std;
        this.bw_radio = bw_radio;
        this.hist_peak = hist_peak;
        this.g_max = g_max;
        this.d_brenner = d_brenner;
        this.d_energy = d_energy;
        this.avg = avg;
        this.pixCount = pixCount;
        this.grayMaxSum = grayMaxSum;
        this.brightSum = brightSum;
        this.brightSum_radio = brightSum_radio;
    }

    public double getEn() {
        return en;
    }

    public void setEn(double en) {
        this.en = en;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    public double getL_std() {
        return l_std;
    }

    public void setL_std(double l_std) {
        this.l_std = l_std;
    }

    public double getBw_radio() {
        return bw_radio;
    }

    public void setBw_radio(double bw_radio) {
        this.bw_radio = bw_radio;
    }

    public double getHist_peak() {
        return hist_peak;
    }

    public void setHist_peak(double hist_peak) {
        this.hist_peak = hist_peak;
    }

    public double getG_max() {
        return g_max;
    }

    public void setG_max(double g_max) {
        this.g_max = g_max;
    }

    public double getD_brenner() {
        return d_brenner;
    }

    public void setD_brenner(double d_brenner) {
        this.d_brenner = d_brenner;
    }

    public double getD_energy() {
        return d_energy;
    }

    public void setD_energy(double d_energy) {
        this.d_energy = d_energy;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getPixCount() {
        return pixCount;
    }

    public void setPixCount(double pixCount) {
        this.pixCount = pixCount;
    }

    public int getGrayMaxSum() {
        return grayMaxSum;
    }

    public void setGrayMaxSum(int grayMaxSum) {
        this.grayMaxSum = grayMaxSum;
    }

    public int getBrightSum() {
        return brightSum;
    }

    public void setBrightSum(int brightSum) {
        this.brightSum = brightSum;
    }

    public double getBrightSum_radio() {
        return brightSum_radio;
    }

    public void setBrightSum_radio(double brightSum_radio) {
        this.brightSum_radio = brightSum_radio;
    }
}
