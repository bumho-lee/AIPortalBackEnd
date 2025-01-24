package esea.esea_api.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import esea.esea_api.entities.Knowledge;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Integer> {
    List<Knowledge> findAllByOrderByActiveDesc();

    List<Knowledge> findAllByKnowledgeIdIn(List<Integer> categoryIds);

    @Query(value = "SELECT * FROM \"KNOWLEDGE\" WHERE :indexcode = ANY(\"INDEX_CODE\")", nativeQuery = true)
    List<Knowledge> findAllByIndexCode(@Param("indexcode") String indexcode);
}
