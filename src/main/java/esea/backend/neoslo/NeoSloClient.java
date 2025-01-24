package esea.backend.neoslo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import esea.backend.neoslo.NeoSloWsImplService;
import esea.backend.neoslo.NeoSloWs;

@Component
@ConditionalOnProperty(name = "neoslo.enabled", havingValue = "true", matchIfMissing = false)
public class NeoSloClient {
    private final NeoSloWs neoSloWs;
    
    public NeoSloClient() {
        NeoSloWsImplService service = new NeoSloWsImplService();
        this.neoSloWs = service.getNeoSloWsImplPort();
    }
    
    public String login(String otaId, String target) {
        return neoSloWs.login(otaId, target);
    }
    
    public String create(String id, String type, String target) {
        return neoSloWs.create(id, type, target);
    }
}