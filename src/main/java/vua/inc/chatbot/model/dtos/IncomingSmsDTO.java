package vua.inc.chatbot.model.dtos;

public record IncomingSmsDTO(
        String id,
        String date,
        String from,
        String linkId,
        String text,
        String to,
        String networkCode
) {
}
