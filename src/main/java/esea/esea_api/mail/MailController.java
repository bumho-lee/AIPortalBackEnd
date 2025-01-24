package esea.esea_api.mail;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import esea.esea_api.mail.dto.MailSendRequestDto;

@Tag(name = "메일", description = "메일 발송")
@RestController 
public class MailController {
    @Autowired
    private MailService mailService;

    @Operation(summary = "메일 발송", description = "메일 발송")
    @PostMapping("/send-mail")
    public Boolean sendMail(@RequestBody MailSendRequestDto request) {
        mailService.sendMail(request);
        return true;
    }
}
