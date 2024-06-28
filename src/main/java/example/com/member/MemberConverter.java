package example.com.member;

import example.com.member.domain.*;
import example.com.member.dto.MemberRequest;
import example.com.member.dto.MemberResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {

    public static Member toMember(MemberRequest.signinRequest request, LoginType loginType) {
        Gender gender = null;
        if (request.getGender().equals("M")) {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }

        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .gender(gender)
                .job(request.getJob())
                .birth(request.getBirth())
                .roleType(RoleType.MEMBER)
                .socialId(request.getSocialId())
                .loginType(loginType)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static MemberResponse.friendListDto toFriendListDto(List<Friend> friendList) {
        List<MemberResponse.friendInfoDto> friendInfoDtoList = friendList.stream()
                .map(MemberConverter::toFriendInfoDto)
                .collect(Collectors.toList());
        return MemberResponse.friendListDto.builder()
                .friendInfoDtoList(friendInfoDtoList)
                .build();

    }

    public static MemberResponse.friendInfoDto toFriendInfoDto(Friend friend) {
        return MemberResponse.friendInfoDto.builder()
                .memberId(friend.getToMember().getId())
                .name(friend.getToMember().getName())
                .build();
    }
}
