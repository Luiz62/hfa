package br.edu.ifg.hfa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {


    private static final Locale LOCALE = new Locale("pt", "BR");

    public static final String PATTERN_DEFAULT = "dd/MM/yyyy";

    public static Date stringToDate(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, LOCALE);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, LOCALE);
        return sdf.format(date);
    }
}
