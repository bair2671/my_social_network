package socialnetwork.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {
    public static String fromDatetoString(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSSSSSX");
        String text = formatter.format(date);
        return text;
    }

    public static Date fromStringToDate(String text) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSSSSSX");
        Date date = new Date();
        try {
            date = formatter.parse(text);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public static LocalDateTime fromDateToLocalDateTime(Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date fromDateToLocalDateTime(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime fromStringRomanianToLocalDateTime(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(text.substring(0,16), formatter);
    }

    public static LocalDateTime fromStringEnglishToLocalDateTime(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(text.substring(0,16), formatter);
    }

    public static String fromLocalDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
