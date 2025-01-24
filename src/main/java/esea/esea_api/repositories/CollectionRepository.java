package esea.esea_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import esea.esea_api.entities.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {

    @Query(value = "SELECT * FROM \"COLLECTION\" WHERE \"COLLECTION_ID\" = :collectionId", nativeQuery = true)
    Collection findByCollectionId(@Param("collectionId") Integer collectionId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO \"COLLECTION\" (\"TYPE\", \"SYSTEM\", \"NAME\", \"USER_ID\", \"STATUS\",\"FOLDER_PATH\", \"METHOD\", \"USE_YN\") " +
                   "VALUES (CAST(:type AS \"COLLECTION_TYPE\"), :system, :name, :userId, :status, :s3Path, :method, :useYN)", nativeQuery = true)
    void createByQuery(@Param("type") String type, 
                     @Param("system") String system, 
                     @Param("name") String name, 
                     @Param("userId") String userId, 
                     @Param("status") String status,
                     @Param("s3Path") String s3Path,
                     @Param("method") String method,
                     @Param("useYN") String useYN);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"COLLECTION\" SET \"TYPE\" = CAST(:type AS \"COLLECTION_TYPE\"), " +
                   "\"SYSTEM\" = :system, \"NAME\" = :name, \"USER_ID\" = :userId, " +
                   "\"FOLDER_PATH\" = :s3Path, \"STATUS\" = :status, \"METHOD\" = :method, \"USE_YN\" = :useYN " +
                   "WHERE \"COLLECTION_ID\" = :collectionId", nativeQuery = true)
    void updateByQuery(@Param("collectionId") Integer collectionId, 
                       @Param("type") String type, 
                       @Param("system") String system, 
                       @Param("name") String name, 
                       @Param("userId") String userId,
                       @Param("s3Path") String s3Path,
                       @Param("status") String status,
                       @Param("method") String method,
                       @Param("useYN") String useYN);

    Page<Collection> findAllByOrderByCollectionIdAsc(Pageable pageable);

    @Query(value = "SELECT * FROM \"COLLECTION\" WHERE \"INDEX_CODE\" = :indexCode LIMIT 1", nativeQuery = true)
    Collection findByIndexCode(@Param("indexCode") String indexCode);
}
