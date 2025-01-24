package esea.esea_api.otp.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetLoginResponse {
    private String setLoginResult;

    @XmlElement
    public String getSetLoginResult() {
        return setLoginResult;
    }

    public void setSetLoginResult(String setLoginResult) {
        this.setLoginResult = setLoginResult;
    }
}
