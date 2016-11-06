package ua.kiev.foxtrot.kopilochka.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by NickNb on 03.11.2016.
 */
public class StringTools {


    public static List<String> ListFromString(String string){
        List<String> list = Arrays.asList(string.split("\\s*,\\s*"));
        return  list;
    }

    public static String StringFromList(List<String> list){
        String string = list.toString().replace("[", "").replace("]", "")
                .replace(", ", ",");
        return string;
    }
}
