package esea.esea_api.otp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import esea.esea_api.otp.dto.SetLoginRequest;

public class SoapClient {

    public static void main(String[] args) {
        try {
            // Define the SOAP endpoint URL
            String url = "http://comm.hanwha-total.com/loginadmincheck/service.asmx";

            // Create the SOAP request XML
            String soapRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
                    + "<soap12:Body>"
                    + "<getLogin xmlns=\"http://jongbu.joo/\">"
                    + "<sysFlag>string</sysFlag>"
                    + "<userId>string</userId>"
                    + "<telePhone>string</telePhone>"
                    + "<sysName>string</sysName>"
                    + "<ipInfo>string</ipInfo>"
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

                    // Output the response (you can parse the response XML if needed)
                    System.out.println("SOAP Response:");
                    System.out.println(response.toString());
                } else {
                    System.out.println("HTTP Error Code: " + responseCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendSoapRequest(SetLoginRequest request) throws Exception {
    	String URL = "http://comm.hanwha-total.com/loginadmincheck/service.asmx";
    	
        String soapEnvelope = createSoapEnvelope(request);
        
        // Create a connection to the SOAP web service
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        connection.setDoOutput(true);
        
        // Send the SOAP request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = soapEnvelope.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Handle the response
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);
    }

    private static String createSoapEnvelope(SetLoginRequest request) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
                "<soap12:Body>" +
                "<setLogin xmlns=\"http://jongbu.joo/\">" +
                "<sSeq>" + request.getSSeq() + "</sSeq>" +
                "<sysFlag>" + request.getSysFlag() + "</sysFlag>" +
                "<userId>" + request.getUserId() + "</userId>" +
                "<sValue>" + request.getSValue() + "</sValue>" +
                "</setLogin>" +
                "</soap12:Body>" +
                "</soap12:Envelope>";
    }
}

