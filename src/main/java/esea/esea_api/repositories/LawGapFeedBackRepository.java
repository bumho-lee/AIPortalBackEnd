package esea.esea_api.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;   
import java.util.List;
import java.util.Optional;

import esea.esea_api.entities.LawGapFeedback;

@Repository
public interface LawGapFeedBackRepository extends JpaRepository<LawGapFeedback, Integer> {
    
    List<LawGapFeedback> findByLawGap_LawGapId(Integer lawGapId);

    Optional<LawGapFeedback> findByUserIdAndLawGap_LawGapId(String userId, Integer lawGapId);

    void deleteByUserIdAndLawGap_LawGapId(String userId, Integer lawGapId);

    List<LawGapFeedback> findByUserId(String userId);
}
