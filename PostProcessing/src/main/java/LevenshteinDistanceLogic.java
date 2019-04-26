import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyuan on 2019/4/26.
 */
public class LevenshteinDistanceLogic {
    public static float compareString(String str, String target)
    {
        float d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        float temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new float[n + 1][m + 1];
        for (i = 0; i <= n; i++)
        {                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++)
        {                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++)
        {                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++)
            {
                ch2 = target.charAt(j - 1);

                temp = compare(String.valueOf(ch1), String.valueOf(ch2));

                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private static  float compare(String input1,String input2)
    {
        for(ENV e : similarList)
        {
            if(e.get_key().equalsIgnoreCase(String.format("%s%s",input1,input2)) || e.get_key().equalsIgnoreCase(String.format("%s%s",input2,input1)))
            {
                return  Float.valueOf(e.get_value());
            }
        }

        if(input1.equalsIgnoreCase(input2))
            return 0;
        else
            return  1;
    }

    public static List<ENV> getSimilarList() {
        return similarList;
    }

    public static void setSimilarList(List<ENV> similarList) {
        LevenshteinDistanceLogic.similarList = similarList;
    }

    private static List<ENV> similarList = new ArrayList() ;

    public static void addSimilar(ENV e)
    {
        similarList.add(e);
    }



    private static float min(float one, float two, float three)
    {
        return (one = one < two ? one : two) < three ? one : three;
    }
}
