package example.com.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import example.com.member.domain.Gender;
import example.com.member.domain.LoginType;

import java.time.LocalDate;

public class MemberResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myInfoDto {
        Long socialId;
        String name;
        String email;
        LoginType loginType;
        String job;
        Gender gender;
        LocalDate birth;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyTypeInsertDto {
        Long studyTypeId;
        String studyTypeTitle;
    }

}
