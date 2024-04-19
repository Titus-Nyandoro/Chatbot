package vua.inc.chatbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vua.inc.chatbot.model.ChatContextExchange;

import java.util.List;

public interface ChatContextRepository extends  JpaRepository<ChatContextExchange,Integer> {
    @Query("SELECT ce.chatMessages FROM ChatContextExchange ce WHERE ce.sessionId = :sessionId")
    List<String> findAllPastHour(@Param("sessionId") String sessionId);
    @Query("DELETE FROM ChatContextExchange ce WHERE ce.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);
   

}
