package vua.inc.chatbot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="chat_context")
@Getter
@Setter
@Builder
public class ChatContextExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String chatMessages;
    private Instant createdAt;
    private String sessionId;

}
