package esea.esea_api.otp;

import java.util.HashMap;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.otp.dto.LoginRequest;
import esea.esea_api.otp.dto.SetLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "OTP", description = "OTP")
@RestController
@RequestMapping("/OTP")
public class LoginController {

    // Endpoint to handle login
    @Operation(summary = "OTP 요청")
    @PostMapping("/getlogin")
    public ResponseEntity<String> getlogin(@RequestBody LoginRequest loginRequest) {

        // Call the SOAP login function with parameters from the request body
        boolean isLoggedIn = SoapLoginClient.login(
                loginRequest.getSysFlag(),
                loginRequest.getUserId(),
                loginRequest.getTelePhone(),
                loginRequest.getSysName(),
                loginRequest.getIpInfo()
        );

        // Return response based on login result
        if (isLoggedIn) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.ok("Login failed.");
        }
    }
    
    @Operation(summary = "OTP 인증")
    @PostMapping("/setlogin")
    public Map<String, String> setlogin(@RequestBody SetLoginRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            // Call the SOAP service to send the request
            SoapClient.sendSoapRequest(request);
            response.put("status", "success");
            response.put("message", "SOAP request sent successfully.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to send SOAP request: " + e.getMessage());
        }

        return response;
    }
}

