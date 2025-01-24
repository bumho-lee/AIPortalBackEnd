package esea.esea_api.repositories;

import esea.esea_api.entities.ConversationReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationReactionRepository extends JpaRepository<ConversationReaction, Integer> {
    ConversationReaction findByConversationId(String conversationId);

    void deleteByConversationId(String conversationId);
}
