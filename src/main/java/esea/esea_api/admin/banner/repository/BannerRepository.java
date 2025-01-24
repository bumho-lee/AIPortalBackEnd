package esea.esea_api.admin.banner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import esea.esea_api.admin.banner.entities.BannerEntService;
import jakarta.transaction.Transactional;

public interface BannerRepository extends JpaRepository<BannerEntService, Integer>  {
	
    @Query(value = "SELECT * FROM \"BANNER_SERVICE\" "
    		+ "WHERE \"NAME\" LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    Page<BannerEntService> findByNameLike(
    		@Param("name") String name,
    		PageRequest pageRequest
    );
    
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM \"BANNER_SERVICE\" "
    		+ "WHERE \"BANNER_SERVICE_ID\" = :bannerServiceId", nativeQuery = true)
    void deleteBannerById(@Param("bannerServiceId") int bannerServiceId);

}
