/**
 * NeoSloWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4.1-SNAPSHOT Nov 07, 2023 (07:57:58 UTC) WSDL2Java emitter.
 */

package hanwha.neo.branch.common.sso.service;

public interface NeoSloWs extends java.rmi.Remote {
    public java.lang.String create(java.lang.String id, java.lang.String type, java.lang.String target) throws java.rmi.RemoteException;
    public java.lang.String login(java.lang.String otaId, java.lang.String target) throws java.rmi.RemoteException;
}
