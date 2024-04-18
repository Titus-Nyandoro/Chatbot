package vua.inc.chatbot.model.dtos;

import lombok.Builder;

@Builder
public record SmsResponse(
        String message
) {
}
