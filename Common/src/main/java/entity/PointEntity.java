package entity;


/**
 * Created by denghuang on 2018/7/4.
 */
public  class PointEntity {


    private int xLeft;

    private int yTop;

    private int xRight;

    private int yButton;

    private int width;

    private int height;

    private double whRadio;

    private boolean isGood;

    private int index;

    private boolean isSpaceText;

    private String doubtWord;

    public String getDoubtWord() {
        return doubtWord;
    }

    public void setDoubtWord(String doubtWord) {
        this.doubtWord = doubtWord;
    }

    public PointEntity(){

    }

    public PointEntity(int xLeft, int yTop, int xRight, int yButton, int width, int height, double whRadio){
        this.xLeft = xLeft;
        this.yTop = yTop;
        this.xRight = xRight;
        this.yButton = yButton;
        this.width = width;
        this.height = height;
        this.whRadio = whRadio;
    }

    public double getWhRadio() {
        return whRadio;
    }

    public void setWhRadio(double whRadio) {
        this.whRadio = whRadio;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getxLeft() {
        return xLeft;
    }

    public void setxLeft(int xLeft) {
        this.xLeft = xLeft;
    }

    public int getyTop() {
        return yTop;
    }

    public void setyTop(int yTop) {
        this.yTop = yTop;
    }

    public int getxRight() {
        return xRight;
    }

    public void setxRight(int xRight) {
        this.xRight = xRight;
    }

    public int getyButton() {
        return yButton;
    }

    public void setyButton(int yButton) {
        this.yButton = yButton;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSpaceText() {
        return isSpaceText;
    }

    public void setSpaceText(boolean spaceText) {
        isSpaceText = spaceText;
    }
}
