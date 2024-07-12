package example.com.member.dto;

import example.com.member.domain.Gender;
import example.com.member.domain.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class friendListDto {
        List<friendInfoDto> friendInfoDtoList;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class friendInfoDto {
        Long memberId;
        String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class memberInfoDto {
        Long memberId;
        String name;
        String profileImg;
    }


}
