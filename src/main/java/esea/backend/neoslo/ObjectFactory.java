
package esea.backend.neoslo;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the esea.backend.neoslo package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _Create_QNAME = new QName("http://service.sso.common.branch.neo.hanwha/", "create");
    private static final QName _CreateResponse_QNAME = new QName("http://service.sso.common.branch.neo.hanwha/", "createResponse");
    private static final QName _Login_QNAME = new QName("http://service.sso.common.branch.neo.hanwha/", "login");
    private static final QName _LoginResponse_QNAME = new QName("http://service.sso.common.branch.neo.hanwha/", "loginResponse");
    private static final QName _LoginResponseReturn_QNAME = new QName("", "return");
    private static final QName _LoginOtaId_QNAME = new QName("", "otaId");
    private static final QName _LoginTarget_QNAME = new QName("", "target");
    private static final QName _CreateId_QNAME = new QName("", "id");
    private static final QName _CreateType_QNAME = new QName("", "type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: esea.backend.neoslo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Create }
     * 
     * @return
     *     the new instance of {@link Create }
     */
    public Create createCreate() {
        return new Create();
    }

    /**
     * Create an instance of {@link CreateResponse }
     * 
     * @return
     *     the new instance of {@link CreateResponse }
     */
    public CreateResponse createCreateResponse() {
        return new CreateResponse();
    }

    /**
     * Create an instance of {@link Login }
     * 
     * @return
     *     the new instance of {@link Login }
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     * @return
     *     the new instance of {@link LoginResponse }
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Create }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Create }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.sso.common.branch.neo.hanwha/", name = "create")
    public JAXBElement<Create> createCreate(Create value) {
        return new JAXBElement<>(_Create_QNAME, Create.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.sso.common.branch.neo.hanwha/", name = "createResponse")
    public JAXBElement<CreateResponse> createCreateResponse(CreateResponse value) {
        return new JAXBElement<>(_CreateResponse_QNAME, CreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Login }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.sso.common.branch.neo.hanwha/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.sso.common.branch.neo.hanwha/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "return", scope = LoginResponse.class)
    public JAXBElement<String> createLoginResponseReturn(String value) {
        return new JAXBElement<>(_LoginResponseReturn_QNAME, String.class, LoginResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "otaId", scope = Login.class)
    public JAXBElement<String> createLoginOtaId(String value) {
        return new JAXBElement<>(_LoginOtaId_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "target", scope = Login.class)
    public JAXBElement<String> createLoginTarget(String value) {
        return new JAXBElement<>(_LoginTarget_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "return", scope = CreateResponse.class)
    public JAXBElement<String> createCreateResponseReturn(String value) {
        return new JAXBElement<>(_LoginResponseReturn_QNAME, String.class, CreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "id", scope = Create.class)
    public JAXBElement<String> createCreateId(String value) {
        return new JAXBElement<>(_CreateId_QNAME, String.class, Create.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "type", scope = Create.class)
    public JAXBElement<String> createCreateType(String value) {
        return new JAXBElement<>(_CreateType_QNAME, String.class, Create.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "target", scope = Create.class)
    public JAXBElement<String> createCreateTarget(String value) {
        return new JAXBElement<>(_LoginTarget_QNAME, String.class, Create.class, value);
    }

}
