package vua.inc.chatbot.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vua.inc.chatbot.model.SMS;

@Repository
public interface SmsRepository extends CrudRepository<SMS, String> {

}
