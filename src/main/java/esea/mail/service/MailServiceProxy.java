package esea.mail.service;

public class MailServiceProxy implements esea.mail.service.MailService {
  private String _endpoint = null;
  private esea.mail.service.MailService mailService = null;
  
  public MailServiceProxy() {
    _initMailServiceProxy();
  }
  
  public MailServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initMailServiceProxy();
  }
  
  private void _initMailServiceProxy() {
    try {
      mailService = (new esea.mail.service.MailServiceServiceLocator()).getMailService();
      if (mailService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mailService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mailService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mailService != null)
      ((javax.xml.rpc.Stub)mailService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public esea.mail.service.MailService getMailService() {
    if (mailService == null)
      _initMailServiceProxy();
    return mailService;
  }
  
  public esea.mail.vo.WsMailStatus[] getMailStatusCounts(java.lang.String[] mailKey) throws java.rmi.RemoteException, esea.common.vo.WsException{
    if (mailService == null)
      _initMailServiceProxy();
    return mailService.getMailStatusCounts(mailKey);
  }
  
  public java.lang.String cancelMISMailByRecipient(java.lang.String mailKey, java.lang.String[] receiverForCancel, esea.mail.vo.WsResource senderInfo) throws java.rmi.RemoteException, esea.common.vo.WsException{
    if (mailService == null)
      _initMailServiceProxy();
    return mailService.cancelMISMailByRecipient(mailKey, receiverForCancel, senderInfo);
  }
  
  public java.lang.String sendMISMail(java.lang.String mailBody, esea.mail.vo.WsMailInfo mailInfo, esea.mail.vo.WsRecipient[] receivers, esea.common.vo.WsAttachFile[] attachFile) throws java.rmi.RemoteException, esea.common.vo.WsException{
    if (mailService == null)
      _initMailServiceProxy();
    return mailService.sendMISMail(mailBody, mailInfo, receivers, attachFile);
  }
  
  
}