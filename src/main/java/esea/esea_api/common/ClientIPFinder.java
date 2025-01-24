package esea.esea_api.common;

import jakarta.servlet.http.HttpServletRequest;

public class ClientIPFinder {
    public static String getClientIP(HttpServletRequest request) {
        String ip = null;
        
        // X-Forwarded-For 헤더 확인 (프록시/로드밸런서 경유)
        ip = request.getHeader("X-Forwarded-For");
        
        // Proxy-Client-IP/WL-Proxy-Client-IP 확인
        if (isInvalidIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (isInvalidIP(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (isInvalidIP(ip)) {
            // 마지막으로 RemoteAddr 확인
            ip = request.getRemoteAddr();
        }
        
        return ip;
    }
    
    private static boolean isInvalidIP(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
