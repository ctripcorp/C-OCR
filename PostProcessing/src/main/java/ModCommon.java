/**
 * Created by zhouyuan on 2018/1/18.
 */
public class ModCommon {
    public static Boolean IsEmptyVar(Object input)
    {
        if (input == null || input.toString().equals("")  )
            return true;
        else
            return false;

    }

    public static Boolean IsCommonPassengerEmptyVar(Object input)
    {
        if (input == null || input.toString().equals("")  || Ns(input).equalsIgnoreCase("Delete_Flag"))
            return true;
        else
            return false;

    }

    public static String Ns(Object input)
    {
        if (IsEmptyVar(input))
            return "";
        else
            return input.toString();
    }


}
