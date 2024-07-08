package example.com.chat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ChatRequest {
    @Getter
    public static class ChatroomCreateRequest {
        @NotNull
        Long targetMemberId;

        @NotNull
        String chatroomType;

        String postUrl;
    }

    @Getter
    public static class ChatCreateRequest {
        @NotEmpty
        String message;
    }
}
