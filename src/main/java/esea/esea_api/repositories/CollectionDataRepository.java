package esea.esea_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import esea.esea_api.entities.CollectionData;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionDataRepository extends JpaRepository<CollectionData, Integer> {
    CollectionData findByCollectionDataId(Integer collectionDataId);

    List<CollectionData> findByCollectionId(Integer collectionId);

    // 파일 이름으로 검색
    CollectionData findByFileName(String fileName);

    // 이름으로 검색
    CollectionData findByName(String name);

    // 파일 이름 부분 검색
    CollectionData findByFileNameContaining(String fileName);

    // 파일 이름으로 Like 검색 (여러 결과 반환)
    List<CollectionData> findByFileNameLike(String fileName);

    // 이름으로 검색 (최신 결과 반환)
    CollectionData findFirstByNameOrderByRegDtDesc(String name);

    // indexFilePath로 검색하는 메소드 추가
    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE normalize(\"INDEX_FILE_PATH\") LIKE normalize(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<CollectionData> findByIndexFilePathContainingIgnoreCase(@Param("keyword") String keyword);

    // 이름 검색
    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE normalize(\"NAME\") = normalize(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<CollectionData> findByNameIgnoreCase(@Param("keyword") String keyword);

    // 파일 이름 검색
    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE normalize(\"FILE_NAME\") = normalize(CONCAT('%', :keyword, '%'))", nativeQuery = true) 
    List<CollectionData> findByFileNameIgnoreCase(@Param("keyword") String keyword);

    // 이름이 일치하는 가장 최신 데이터 하나만 반환
    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE normalize(\"NAME\") = normalize(:keyword) ORDER BY \"COLLECTION_DATA_ID\" DESC LIMIT 1", nativeQuery = true)
    CollectionData findLatestByNameIgnoreCase(@Param("keyword") String keyword);

    // 파일 이름 일치하는 가장 최신 데이터 하나만 반환
    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE normalize(\"FILE_NAME\") = normalize(:keyword) ORDER BY \"COLLECTION_DATA_ID\" DESC LIMIT 1", nativeQuery = true)
    CollectionData findLatestByFileNameIgnoreCase(@Param("keyword") String keyword);

    // 인덱스 파일 이름 검색 중 가장 쵯신 데이터 하나만 반환
    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE normalize(\"INDEX_FILE_PATH\") LIKE normalize(CONCAT('%', :keyword, '%')) ORDER BY \"COLLECTION_DATA_ID\" DESC LIMIT 1", nativeQuery = true)
    CollectionData findLatestByIndexFilePathIgnoreCase(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM \"COLLECTION_DATA\" WHERE \"COLLECTION_ID\" = :collectionId " +
               "AND (CAST(:startDate AS timestamptz) IS NULL OR \"REG_DT\" >= CAST(:startDate AS timestamptz)) " +
               "AND (CAST(:endDate AS timestamptz) IS NULL OR \"REG_DT\" <= CAST(:endDate AS timestamptz)) " +
               "AND (CAST(:keyword AS text) IS NULL OR \"NAME\" LIKE '%' || CAST(:keyword AS text) || '%' " +
               "OR \"FILE_NAME\" LIKE '%' || CAST(:keyword AS text) || '%') " +
               "ORDER BY \"COLLECTION_DATA_ID\" DESC",
       countQuery = "SELECT COUNT(*) FROM \"COLLECTION_DATA\" WHERE \"COLLECTION_ID\" = :collectionId " +
                    "AND (CAST(:startDate AS timestamptz) IS NULL OR \"REG_DT\" >= CAST(:startDate AS timestamptz)) " +
                    "AND (CAST(:endDate AS timestamptz) IS NULL OR \"REG_DT\" <= CAST(:endDate AS timestamptz)) " +
                    "AND (CAST(:keyword AS text) IS NULL OR \"NAME\" LIKE '%' || CAST(:keyword AS text) || '%' " +
                    "OR \"FILE_NAME\" LIKE '%' || CAST(:keyword AS text) || '%')",
       nativeQuery = true)
    Page<CollectionData> findByDateAndKeyword(
        @Param("collectionId") Integer collectionId,
        @Param("startDate") OffsetDateTime startDate,
        @Param("endDate") OffsetDateTime endDate,
        @Param("keyword") String keyword,
        Pageable pageable
    );


    long countByCollectionId(Integer collectionId);
}

