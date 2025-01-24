package esea.esea_api.admin.banner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.banner.entities.BannerEntService;
import esea.esea_api.admin.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;

//Service
@Service
@RequiredArgsConstructor
public class BannerService {
 private final BannerRepository bannerRepository;
 
 	public Page<BannerEntService> searchBannerServiceByName(
         String name,
         int page,
         int size
     ){
     // 페이징 설정
     PageRequest pageRequest = PageRequest.of(page -1, size);
 	
     return bannerRepository.findByNameLike(
 		name != null ? name : "",
         pageRequest
     );
 	}
 
	public List<BannerEntService> findAllBannerServices() {
	    return bannerRepository.findAll();  // This will return all the records from the database
	}
	
	public BannerEntService saveBannerEntService(BannerEntService bannerEntService) {
		return bannerRepository.save(bannerEntService);
	}
	 
	public Optional<BannerEntService> getBannerById(int bannerServiceId) {
	    return bannerRepository.findById(bannerServiceId);
	}
	 
	public BannerEntService saveBanner(BannerEntService bannerEntService) {
	    return bannerRepository.save(bannerEntService);
	}
	 
    public Map<String, Object> deleteBanners(List<Integer> bannerServiceIds) {
        Map<String, Object> response = new HashMap<>();
        List<Integer> successfulDeletes = new ArrayList<>();
        List<Map<String, Object>> failedDeletes = new ArrayList<>();

        for (Integer bannerId : bannerServiceIds) {
            try {
                Optional<BannerEntService> bannerOpt = bannerRepository.findById(bannerId);
                
                if (bannerOpt.isPresent()) {
                    BannerEntService banner = bannerOpt.get();
                    
                    // DB에서 배너 삭제
                    bannerRepository.deleteById(bannerId);
                    successfulDeletes.add(bannerId);
                    
                } else {
                    Map<String, Object> failInfo = new HashMap<>();
                    failInfo.put("bannerId", bannerId);
                    failInfo.put("error", "배너를 찾을 수 없습니다.");
                    failedDeletes.add(failInfo);
                }
                
            } catch (Exception e) {
                Map<String, Object> failInfo = new HashMap<>();
                failInfo.put("bannerId", bannerId);
                failInfo.put("error", "삭제 중 오류 발생: " + e.getMessage());
                failedDeletes.add(failInfo);
            }
        }

        // 응답 데이터 설정
        response.put("success", successfulDeletes);
        response.put("successCount", successfulDeletes.size());
        response.put("failed", failedDeletes);
        response.put("failCount", failedDeletes.size());
        response.put("totalRequested", bannerServiceIds.size());
        
        if (!failedDeletes.isEmpty()) {
            response.put("message", "일부 배너 삭제 실패");
        } else {
            response.put("message", "모든 배너 삭제 성공");
        }

        return response;
    }
}
