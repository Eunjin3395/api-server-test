package example.com.chat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ChatRequest {
    @Getter
    public static class ChatCreateRequest {
        @NotNull
        Long targetMemberId;

        @NotNull
        String chatroomType;

        String postUrl;
    }
}
