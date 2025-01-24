package esea.esea_api.mail;

import esea.mail.service.MailServiceProxy;
import esea.mail.vo.WsMailInfo;
import esea.mail.vo.WsRecipient;
import esea.common.vo.WsAttachFile;

import esea.esea_api.mail.dto.MailSendRequestDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class MailService {

    @Autowired
    private TemplateEngine templateEngine;

    public boolean sendMail(MailSendRequestDto request) {
        MailServiceProxy proxy = new MailServiceProxy();

        // 메일 내용
        WsMailInfo mailInfo = new WsMailInfo();
        mailInfo.setSubject(request.getSubject());
        mailInfo.setHtmlContent(true); // HTML 콘텐츠 사용
        mailInfo.setMhtContent(false);
        mailInfo.setAttachCount(0);
        mailInfo.setSenderEmail(request.getFrom());
        mailInfo.setImportant(false);

        // 메일 수신자
        WsRecipient[] receivers = new WsRecipient[request.getTo().size()];
        for (int i = 0; i < request.getTo().size(); i++) {
            receivers[i] = new WsRecipient();
            receivers[i].setRecvEmail(request.getTo().get(i));
            receivers[i].setSeqID(i);
            receivers[i].setRecvType("TO");
            receivers[i].setDept(false);
        }

        // 메일 첨부파일
        WsAttachFile[] attachFile = new WsAttachFile[0];

        // 템플릿 엔진을 사용하여 HTML 콘텐츠 생성
        Context context = new Context();
        context.setVariable("serviceName", request.getParams().get("serviceName"));
        context.setVariable("baseDate", request.getParams().get("baseDate"));
        context.setVariable("count", request.getParams().get("count"));
        String htmlContent = templateEngine.process("emailTemplate", context);

        // 메일 발송
        try {
            proxy.sendMISMail(htmlContent, mailInfo, receivers, attachFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
