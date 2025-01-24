package esea.esea_api.translation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esea.esea_api.translation.entities.CommonCode;
import esea.esea_api.translation.repository.TranslationLanRepository;

@Service
public class TranslationLanService {

    private final TranslationLanRepository translationLanRepository;

    @Autowired
    public TranslationLanService(TranslationLanRepository translationLanRepository) {
        this.translationLanRepository = translationLanRepository;
    }
    
    public List<CommonCode> getCommonCodesByCodeType(String codeType) {
        return translationLanRepository.findByCodeType(codeType);
    }
}
