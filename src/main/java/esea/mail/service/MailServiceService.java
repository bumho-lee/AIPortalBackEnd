/**
 * MailServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4.1-SNAPSHOT Nov 07, 2023 (07:57:58 UTC) WSDL2Java emitter.
 */

package esea.mail.service;

public interface MailServiceService extends javax.xml.rpc.Service {
    public java.lang.String getMailServiceAddress();

    public esea.mail.service.MailService getMailService() throws javax.xml.rpc.ServiceException;

    public esea.mail.service.MailService getMailService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
