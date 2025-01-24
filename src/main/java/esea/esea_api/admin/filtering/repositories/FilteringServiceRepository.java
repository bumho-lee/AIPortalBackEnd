package esea.esea_api.admin.filtering.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.filtering.entities.FilteringService;

@Repository
public interface FilteringServiceRepository extends JpaRepository<FilteringService, Integer> {
    // KEYWORD LIKE 조회
    @Query(value = "SELECT * FROM \"FILTERING_SERVICE\" " 
            + "WHERE \"KEYWORD\" LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    Page<FilteringService> findByKeywordLike(
            @Param("keyword") String keyword,
            PageRequest pageRequest
    );
}
