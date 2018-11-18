package es.source.code.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    /**
          * 验证输入格式是否合法
          * @param number
          * @return
          */
    public static boolean validateFormat(String number) {
        Pattern p = Pattern.compile("^[A-Za-z1-9_-]+$");//正则表达式
        Matcher m = p.matcher(number);
        return m.matches();
    }
}
