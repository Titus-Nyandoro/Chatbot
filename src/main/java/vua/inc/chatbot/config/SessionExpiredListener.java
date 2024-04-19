package vua.inc.chatbot.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import vua.inc.chatbot.repo.ChatContextRepository;
@RequiredArgsConstructor
public class SessionExpiredListener implements HttpSessionListener {
    private final ChatContextRepository chatContextRepository;
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        chatContextRepository.deleteBySessionId(se.getSession().getId());
    }

}

