package esea.esea_api.otp;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SoapLoginClient {

    // Method to perform the login using SOAP request and handle the response
    public static boolean login(String sysFlag, String userId, String telePhone, String sysName, String ipInfo) {
        try {
            // Define the SOAP endpoint URL
            String url = "http://comm.hanwha-total.com/loginadmincheck/service.asmx";

            // Create the SOAP request XML
            String soapRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
                    + "<soap12:Body>"
                    + "<getLogin xmlns=\"http://jongbu.joo/\">"
                    + "<sysFlag>" + sysFlag + "</sysFlag>"
                    + "<userId>" + userId + "</userId>"
                    + "<telePhone>" + telePhone + "</telePhone>"
                    + "<sysName>" + sysName + "</sysName>"
                    + "<ipInfo>" + ipInfo + "</ipInfo>"
                    + "</getLogin>"
                    + "</soap12:Body>"
                    + "</soap12:Envelope>";

            // Create a URL object for the SOAP service
            URL endpoint = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

            // Set the necessary HTTP headers for SOAP request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            connection.setRequestProperty("Accept", "application/soap+xml; charset=utf-8");
            connection.setDoOutput(true);

            // Send the SOAP request
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = soapRequest.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            // Get the SOAP response
            try (InputStream responseStream = connection.getInputStream()) {
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    int byteRead;
                    while ((byteRead = responseStream.read()) != -1) {
                        response.append((char) byteRead);
                    }

                    // Parse the response XML to extract 'getLoginResult'
                    String responseXML = response.toString();
                    return parseLoginResponse(responseXML);
                } else {
                    System.out.println("HTTP Error Code: " + responseCode);
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to parse the SOAP response and check the login result
    private static boolean parseLoginResponse(String responseXML) {
        try {
            // Parse the response XML to extract getLoginResult
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(responseXML)));

            // Extract the 'getLoginResult' element value
            NodeList resultNodes = doc.getElementsByTagNameNS("http://jongbu.joo/", "getLoginResult");
            if (resultNodes.getLength() > 0) {
                String loginResult = resultNodes.item(0).getTextContent();
                System.out.println("getLoginResult: " + loginResult);
                return "success".equalsIgnoreCase(loginResult); // Assuming "success" means login is successful
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
