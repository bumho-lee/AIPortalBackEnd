package esea.esea_api.translation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.translation.dto.TranslationFileSearchDTO;
import esea.esea_api.translation.dto.TranslationLanSearchDTO;
import esea.esea_api.translation.entities.CommonCode;
import esea.esea_api.translation.entities.TranslationFile;
import esea.esea_api.translation.service.TranslationFileService;
import esea.esea_api.translation.service.TranslationLanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "번역 조회 API", description = "번역 조회 API")
@RestController
@RequestMapping("/translate")
public class TranslationLanController {

    private final TranslationLanService translationLanService;

    @Autowired
    public TranslationLanController(TranslationLanService translationLanService) {
        this.translationLanService = translationLanService;
    }
    
    @Operation(summary = "언어 조회", description = "언어를 조회합니다.")
    @PostMapping("/lan")
    public List<CommonCode> getLan(@RequestBody(required = false) TranslationLanSearchDTO searchDTO) {
        String codeType = searchDTO.getCodeType();
        
        return translationLanService.getCommonCodesByCodeType(codeType);
    }

}
