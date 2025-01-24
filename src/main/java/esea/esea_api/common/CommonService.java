package esea.esea_api.common;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class CommonService {

    /**
     * XML을 JSON으로 변환하는 메서드입니다.
     * 
     * @param xmlContent XML 문자열
     * @return JsonNode 객체
     * @throws Exception 변환 중 발생한 예외
     */
    public JsonNode convertXmlToJson(String xmlContent) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = xmlMapper.readTree(xmlContent);
        return new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(jsonNode));
    }

    public JsonNode convertStringToJson(String jsonContent) throws Exception {
        return new ObjectMapper().readTree(jsonContent);
    }
}
