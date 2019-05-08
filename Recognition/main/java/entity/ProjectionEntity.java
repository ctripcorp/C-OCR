package entity;

public class ProjectionEntity {
    public ProjectionEntity(Integer index,Integer index1, Integer index2, Double num, Double signNum) {
        this.index = index;
        this.index1 = index1;
        this.index2 = index2;
        this.num = num;
        this.signNum = signNum;
    }

    private Integer index;
    private Integer index1;
    private Integer index2;
    private Double num;
    private Double signNum;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getSignNum() {
        return signNum;
    }

    public void setSignNum(Double signNum) {
        this.signNum = signNum;
    }

    public Integer getIndex1() {
        return index1;
    }

    public void setIndex1(Integer index1) {
        this.index1 = index1;
    }

    public Integer getIndex2() {
        return index2;
    }

    public void setIndex2(Integer index2) {
        this.index2 = index2;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }
}
