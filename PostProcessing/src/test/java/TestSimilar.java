import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyuan on 2019/4/26.
 */
public class TestSimilar {
    public static void main(String[] args) {
        ENV e = new ENV("0O","0.3");
        LevenshteinDistanceLogic.addSimilar(e);
        e = new ENV("38","0.5");
        LevenshteinDistanceLogic.addSimilar(e);


        float i =  LevenshteinDistanceLogic.compareString("coffee", "cafe");

        System.out.println(i);
    }

    private static List<ENV> similarList = new ArrayList() ;
}
