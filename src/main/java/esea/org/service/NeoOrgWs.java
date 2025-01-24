/**
 * NeoOrgWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4.1-SNAPSHOT Nov 07, 2023 (07:57:58 UTC) WSDL2Java emitter.
 */

package esea.org.service;

public interface NeoOrgWs extends java.rmi.Remote {
    public esea.org.vo.OrgUserVO[] searchUserByUserIdListIncConcurrentPosition(java.lang.String[] userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgDeptVO[] searchDeptByDeptIdIncludeChild(java.lang.String deptId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO searchDefaultUserByUserId(java.lang.String userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgDeptVO[] searchDeptByDeptName(java.lang.String deptName) throws java.rmi.RemoteException;
    public esea.org.vo.OrgCodeVO[] getOrgClassList() throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserKeyListIncConcurrentPosition(java.lang.String[] userKey) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByOrgFunctionId(java.lang.String orgFunctionId) throws java.rmi.RemoteException;
    public java.lang.String checkDuplicatedUserId(java.lang.String userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserKey(java.lang.String userKey) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByOrgClassId(java.lang.String orgClassId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgDeptVO[] searchDeptByCompanyId() throws java.rmi.RemoteException;
    public esea.org.vo.OrgDeptVO[] searchDeptByCompanyIdForPaging(int pageNo, int pageSize) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByOrgPositionIdIncConcurrentPosition(java.lang.String orgPositionId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchGroupUserByEmpolyeeNo(java.lang.String employeeNo) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByEmpolyeeNo(java.lang.String employeeNo) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByDeptIdForPaging(java.lang.String deptId, int pageNo, int pageSize) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByMailList(java.lang.String[] email) throws java.rmi.RemoteException;
    public esea.org.vo.OrgCodeVO[] getOrgPositionList() throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByMailIncConcurrentPosition(java.lang.String email) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserKeyIncConcurrentPosition(java.lang.String userKey) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByOrgPositionId(java.lang.String orgPositionId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByDeptId(java.lang.String deptId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserNameIncConcurrentPosition(java.lang.String userName) throws java.rmi.RemoteException;
    public esea.org.vo.OrgCompanyVO[] getGroupCompanyList() throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByDeptIdIncConcurrentPosition(java.lang.String deptId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchGroupUserByUserId(java.lang.String userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgDeptVO[] searchDeptByDeptId(java.lang.String deptId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserIdList(java.lang.String[] userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByOrgClassIdIncConcurrentPosition(java.lang.String orgClassId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByOrgFunctionIdIncConcurrentPosition(java.lang.String orgFunctionId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByEmpolyeeNoIncConcurrentPosition(java.lang.String employeeNo) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByMail(java.lang.String email) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserName(java.lang.String userName) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserId(java.lang.String userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserIdIncConcurrentPosition(java.lang.String userId) throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByUserKeyList(java.lang.String[] userKey) throws java.rmi.RemoteException;
    public esea.org.vo.OrgCodeVO[] getOrgFunctionList() throws java.rmi.RemoteException;
    public esea.org.vo.OrgUserVO[] searchUserByMailListIncConcurrentPosition(java.lang.String[] email) throws java.rmi.RemoteException;
}
