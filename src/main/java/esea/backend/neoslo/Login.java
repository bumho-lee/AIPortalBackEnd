
package esea.backend.neoslo;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>login complex type에 대한 Java 클래스입니다.</p>
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.</p>
 * 
 * <pre>{@code
 * <complexType name="login">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="otaId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="target" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "login", propOrder = {
    "otaId",
    "target"
})
public class Login {

    @XmlElementRef(name = "otaId", type = JAXBElement.class, required = false)
    protected JAXBElement<String> otaId;
    @XmlElementRef(name = "target", type = JAXBElement.class, required = false)
    protected JAXBElement<String> target;

    /**
     * otaId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOtaId() {
        return otaId;
    }

    /**
     * otaId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOtaId(JAXBElement<String> value) {
        this.otaId = value;
    }

    /**
     * target 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTarget() {
        return target;
    }

    /**
     * target 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTarget(JAXBElement<String> value) {
        this.target = value;
    }

}
