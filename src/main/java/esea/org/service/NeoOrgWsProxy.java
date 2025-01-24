package esea.org.service;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class NeoOrgWsProxy implements esea.org.service.NeoOrgWs {
  private String _endpoint;
  private esea.org.service.NeoOrgWs neoOrgWs;
  
  public NeoOrgWsProxy() {
    _endpoint = System.getenv("ORG_END_POINT");
    
    _initNeoOrgWsProxy();
  }

  private void _initNeoOrgWsProxy() {
    try {
      neoOrgWs = (new esea.org.service.NeoOrgWsImplServiceLocator()).getNeoOrgWsImplPort();

      if (neoOrgWs != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)neoOrgWs)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)neoOrgWs)._getProperty("javax.xml.rpc.service.endpoint.address");
      } 
    } catch (javax.xml.rpc.ServiceException serviceException) {
      throw new RuntimeException("NeoOrgWs 초기화 실패: " + serviceException.getMessage(), serviceException);
    }
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (neoOrgWs != null)
      ((javax.xml.rpc.Stub)neoOrgWs)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public esea.org.service.NeoOrgWs getNeoOrgWs() {
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs;
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserIdListIncConcurrentPosition(java.lang.String[] userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserIdListIncConcurrentPosition(userId);
  }
  
  public esea.org.vo.OrgDeptVO[] searchDeptByDeptIdIncludeChild(java.lang.String deptId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchDeptByDeptIdIncludeChild(deptId);
  }
  
  public esea.org.vo.OrgUserVO searchDefaultUserByUserId(java.lang.String userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchDefaultUserByUserId(userId);
  }
  
  public esea.org.vo.OrgDeptVO[] searchDeptByDeptName(java.lang.String deptName) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchDeptByDeptName(deptName);
  }
  
  public esea.org.vo.OrgCodeVO[] getOrgClassList() throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.getOrgClassList();
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserKeyListIncConcurrentPosition(java.lang.String[] userKey) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserKeyListIncConcurrentPosition(userKey);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByOrgFunctionId(java.lang.String orgFunctionId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByOrgFunctionId(orgFunctionId);
  }
  
  public java.lang.String checkDuplicatedUserId(java.lang.String userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.checkDuplicatedUserId(userId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserKey(java.lang.String userKey) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserKey(userKey);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByOrgClassId(java.lang.String orgClassId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByOrgClassId(orgClassId);
  }
  
  public esea.org.vo.OrgDeptVO[] searchDeptByCompanyId() throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchDeptByCompanyId();
  }
  
  public esea.org.vo.OrgDeptVO[] searchDeptByCompanyIdForPaging(int pageNo, int pageSize) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchDeptByCompanyIdForPaging(pageNo, pageSize);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByOrgPositionIdIncConcurrentPosition(java.lang.String orgPositionId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByOrgPositionIdIncConcurrentPosition(orgPositionId);
  }
  
  public esea.org.vo.OrgUserVO[] searchGroupUserByEmpolyeeNo(java.lang.String employeeNo) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchGroupUserByEmpolyeeNo(employeeNo);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByEmpolyeeNo(java.lang.String employeeNo) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByEmpolyeeNo(employeeNo);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByDeptIdForPaging(java.lang.String deptId, int pageNo, int pageSize) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByDeptIdForPaging(deptId, pageNo, pageSize);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByMailList(java.lang.String[] email) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByMailList(email);
  }
  
  public esea.org.vo.OrgCodeVO[] getOrgPositionList() throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.getOrgPositionList();
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByMailIncConcurrentPosition(java.lang.String email) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByMailIncConcurrentPosition(email);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserKeyIncConcurrentPosition(java.lang.String userKey) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserKeyIncConcurrentPosition(userKey);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByOrgPositionId(java.lang.String orgPositionId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByOrgPositionId(orgPositionId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByDeptId(java.lang.String deptId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByDeptId(deptId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserNameIncConcurrentPosition(java.lang.String userName) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserNameIncConcurrentPosition(userName);
  }
  
  public esea.org.vo.OrgCompanyVO[] getGroupCompanyList() throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.getGroupCompanyList();
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByDeptIdIncConcurrentPosition(java.lang.String deptId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByDeptIdIncConcurrentPosition(deptId);
  }
  
  public esea.org.vo.OrgUserVO[] searchGroupUserByUserId(java.lang.String userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchGroupUserByUserId(userId);
  }
  
  public esea.org.vo.OrgDeptVO[] searchDeptByDeptId(java.lang.String deptId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchDeptByDeptId(deptId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserIdList(java.lang.String[] userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserIdList(userId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByOrgClassIdIncConcurrentPosition(java.lang.String orgClassId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByOrgClassIdIncConcurrentPosition(orgClassId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByOrgFunctionIdIncConcurrentPosition(java.lang.String orgFunctionId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByOrgFunctionIdIncConcurrentPosition(orgFunctionId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByEmpolyeeNoIncConcurrentPosition(java.lang.String employeeNo) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByEmpolyeeNoIncConcurrentPosition(employeeNo);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByMail(java.lang.String email) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByMail(email);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserName(java.lang.String userName) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserName(userName);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserId(java.lang.String userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserId(userId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserIdIncConcurrentPosition(java.lang.String userId) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserIdIncConcurrentPosition(userId);
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByUserKeyList(java.lang.String[] userKey) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByUserKeyList(userKey);
  }
  
  public esea.org.vo.OrgCodeVO[] getOrgFunctionList() throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.getOrgFunctionList();
  }
  
  public esea.org.vo.OrgUserVO[] searchUserByMailListIncConcurrentPosition(java.lang.String[] email) throws java.rmi.RemoteException{
    if (neoOrgWs == null)
      _initNeoOrgWsProxy();
    return neoOrgWs.searchUserByMailListIncConcurrentPosition(email);
  }
  
  
}