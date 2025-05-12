package ro.adela.bank.utils;

public class StringUtils {

    public static boolean isBlank(String text) {
        return (text == null || text.isBlank());
    }

    public static boolean isEmpty(String text) {
        return (text == null || text.isEmpty());
    }
}
