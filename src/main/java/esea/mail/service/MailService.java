/**
 * MailService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4.1-SNAPSHOT Nov 07, 2023 (07:57:58 UTC) WSDL2Java emitter.
 */

package esea.mail.service;

public interface MailService extends java.rmi.Remote {
    public esea.mail.vo.WsMailStatus[] getMailStatusCounts(java.lang.String[] mailKey) throws java.rmi.RemoteException, esea.common.vo.WsException;
    public java.lang.String cancelMISMailByRecipient(java.lang.String mailKey, java.lang.String[] receiverForCancel, esea.mail.vo.WsResource senderInfo) throws java.rmi.RemoteException,esea.common.vo.WsException;
    public java.lang.String sendMISMail(java.lang.String mailBody, esea.mail.vo.WsMailInfo mailInfo, esea.mail.vo.WsRecipient[] receivers, esea.common.vo.WsAttachFile[] attachFile) throws java.rmi.RemoteException, esea.common.vo.WsException;
}
