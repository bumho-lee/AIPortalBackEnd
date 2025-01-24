package esea.esea_api.otp.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetLoginRequest {
    private String sSeq;
    private String sysFlag;
    private String userId;
    private String sValue;

    @XmlElement
    public String getSSeq() {
        return sSeq;
    }

    public void setSSeq(String sSeq) {
        this.sSeq = sSeq;
    }

    @XmlElement
    public String getSysFlag() {
        return sysFlag;
    }

    public void setSysFlag(String sysFlag) {
        this.sysFlag = sysFlag;
    }

    @XmlElement
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @XmlElement
    public String getSValue() {
        return sValue;
    }

    public void setSValue(String sValue) {
        this.sValue = sValue;
    }
}

