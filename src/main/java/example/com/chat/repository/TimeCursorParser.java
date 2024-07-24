package example.com.chat.repository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class TimeCursorParser {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime decode(String cursor) {
        // Base64 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(cursor);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        // LocalDateTime으로 변환
        return LocalDateTime.parse(decodedString, formatter);
    }

    public static String encodeLocalDateTime(LocalDateTime localDateTime) {
        // LocalDateTime을 문자열로 변환
        String dateTimeString = localDateTime.format(formatter);

        // 문자열을 Base64로 인코딩
        return Base64.getEncoder().encodeToString(dateTimeString.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(String string) {
        // 문자열을 Base64로 인코딩
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

}
