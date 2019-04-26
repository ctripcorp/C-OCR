package util;


import entity.PointEntity;
import entity.PointLocationEntity;

public class ConvertUtil {

    public static PointEntity convertPoint(int x1, int x2, int y1, int y2){

        PointEntity p = null;
        if(x2>x1 && x1>=0 && y2>y1 && y1>=0){
            int w = x2-x1+1;
            int h = y2 -y1+1;
            double whRadio = w/(double)h;
            p = new PointEntity(x1,y1,x2,y2,w,w,whRadio);
        }
        return  p;

    }

    // convert
    public static PointLocationEntity convert(int pointLocation, boolean isLeft){

        PointLocationEntity pl = new PointLocationEntity();
        pl.pointLocation = pointLocation;
        pl.isLeft = isLeft;
        return pl;

    }



}
