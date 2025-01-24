package esea.prodSlo.service;

import java.rmi.RemoteException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import hanwha.neo.branch.common.sso.service.NeoSloWs;
import hanwha.neo.branch.common.sso.service.NeoSloWsImplService;
import hanwha.neo.branch.common.sso.service.NeoSloWsImplServiceLocator;

@Component
@ConditionalOnProperty(name = "neoslo.enabled", havingValue = "true", matchIfMissing = false)
public class NeoProdSloClient {
    private final NeoSloWs neoSloWs;
    
    public NeoProdSloClient() throws javax.xml.rpc.ServiceException {
        NeoSloWsImplService service = new NeoSloWsImplServiceLocator();
        this.neoSloWs = service.getNeoSloWsImplPort();
    }
    
    public String login(String otaId, String target) throws RemoteException {
        System.out.println("NeoProdSloClient.login() : " + otaId + " / " + target);

        return neoSloWs.login(otaId, target);
    }
    
    public String create(String id, String type, String target) throws RemoteException {
        return neoSloWs.create(id, type, target);
    }
}