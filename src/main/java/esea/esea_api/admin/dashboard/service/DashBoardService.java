package esea.esea_api.admin.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.dashboard.dto.DashBoardChatDto;
import esea.esea_api.admin.dashboard.dto.DashBoardTokenDto;
import esea.esea_api.admin.dashboard.dto.DashBoardTransDto;
import esea.esea_api.admin.dashboard.dto.DashBoardUserDto;
import esea.esea_api.admin.dashboard.repository.DashBoardRepository;

@Service
public class DashBoardService {
	
	private final DashBoardRepository dashBoardRepository;
	
    @Autowired
    public DashBoardService(DashBoardRepository dashBoardRepository) {
        this.dashBoardRepository = dashBoardRepository;
    }
	
    public List<DashBoardUserDto> dashBoardUserWeekCount(String startDate, String endDate) {

        List<Object[]> rawData = dashBoardRepository.dashBoardUserWeekCount(startDate, endDate);
        List<DashBoardUserDto> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
            String userDate = (String) data[0];
            Long userCount = (Long) data[1];

            DashBoardUserDto response = new DashBoardUserDto(userDate, userCount);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<DashBoardChatDto> dashBoardChatWeekCount(String startDate, String endDate) {
        List<Object[]> rawData = dashBoardRepository.dashBoardChatWeekCount(startDate, endDate);
        List<DashBoardChatDto> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
            String chatDate = (String) data[0];
            Long chatCount = (Long) data[1];

            DashBoardChatDto response = new DashBoardChatDto(chatDate, chatCount);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<DashBoardUserDto> dashBoardUserMonthCount(String startDate, String endDate) {
        List<Object[]> rawData = dashBoardRepository.dashBoardUserMonthCount(startDate, endDate);
        List<DashBoardUserDto> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {

            String userDate = (String) data[0];
            Long userCount = (Long) data[1];

            DashBoardUserDto response = new DashBoardUserDto(userDate, userCount);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<DashBoardChatDto> dashBoardChatMonthCount(String startDate, String endDate) {
        List<Object[]> rawData = dashBoardRepository.dashBoardChatMonthCount(startDate, endDate);
        List<DashBoardChatDto> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
            String chatDate = (String) data[0];
            Long chatCount = (Long) data[1];

            DashBoardChatDto response = new DashBoardChatDto(chatDate, chatCount);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<DashBoardUserDto> dashBoardUserCount(String sMonth, String eMonth) {
        List<Object[]> rawData = dashBoardRepository.dashBoardUserCount(sMonth, eMonth);
        List<DashBoardUserDto> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
            String date = (String) data[0];
            Long count = (Long) data[1];

            DashBoardUserDto response = new DashBoardUserDto(date, count);
            responses.add(response); 
        }
        
        return responses;
    }

    public List<DashBoardChatDto> dashBoardChatCount(String sMonth, String eMonth) {
        // Pass the sMonth and eMonth parameters to the repository query
        List<Object[]> rawData = dashBoardRepository.dashBoardChatCount(sMonth, eMonth);
        List<DashBoardChatDto> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
            String date = (String) data[0];
            Long count = (Long) data[1];

            DashBoardChatDto response = new DashBoardChatDto(date, count);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<DashBoardTokenDto> dashBoardTokenCount(String llmModels, String startDate, String endDate) {
        // Pass the sMonth and eMonth parameters to the repository query
        List<Object[]> rawData = dashBoardRepository.dashBoardTokenCount(llmModels, startDate, endDate);
        List<DashBoardTokenDto> responses = new ArrayList<>();

        for (Object[] data : rawData) {
            String tokenDate = (String) data[0];
            String llmModel = llmModels;
            Long tokenInputCount = (Long) data[2];
            Long tokenOutPutCount = (Long) data[3];
            Long tokenRecordCount = (Long) data[4];
            Long tokenSum  = (Long) data[5];

            DashBoardTokenDto response = new DashBoardTokenDto(tokenDate, llmModel, tokenInputCount, tokenOutPutCount, tokenRecordCount, tokenSum);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<DashBoardTransDto> findMonthlyTranslationSummary(String startDate, String endDate) {
    	List<Object[]> rawData = dashBoardRepository.findMonthlyTranslationSummary(startDate, endDate);
        List<DashBoardTransDto> responses = new ArrayList<>();
    	
        for (Object[] data : rawData) {
            String regDt = (String) data[0];
            Long translatedCharcnt = (Long) data[1];

            DashBoardTransDto response = new DashBoardTransDto(regDt, translatedCharcnt);
            responses.add(response);
        }
        
    	return responses; 
    }

}
