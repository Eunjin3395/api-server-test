package example.com.utils;

import java.time.Instant;

public class timestamp {

    public static long getNowUtcTimeStamp() {
        // 현재 UTC 시간 기준 타임스탬프 생성
        Instant now = Instant.now();
        return now.toEpochMilli();
    }

}
