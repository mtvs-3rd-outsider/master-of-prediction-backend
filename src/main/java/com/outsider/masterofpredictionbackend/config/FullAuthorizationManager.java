package com.outsider.masterofpredictionbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class FullAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final WebSecurityService webSecurityService;

    public FullAuthorizationManager(WebSecurityService webSecurityService) {
        this.webSecurityService = webSecurityService;
    }



    private Long extractUserIdFromRequest(HttpServletRequest request) {
        // 요청 URI에서 /users/{userid}/ 패턴을 찾아 {userid} 부분을 추출
        String path = request.getRequestURI();
        String[] pathParts = path.split("/");

        // "users"라는 경로를 찾고 그 다음의 경로가 userId라고 가정
        for (int i = 0; i < pathParts.length; i++) {
            if (pathParts[i].equalsIgnoreCase("users") && i + 1 < pathParts.length) {
                try {
                    return Long.parseLong(pathParts[i + 1]); // 다음 부분을 userId로 파싱
                } catch (NumberFormatException e) {
                    // userId가 숫자가 아닐 경우 null 반환
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        // Supplier에서 Authentication 객체를 가져옴
        Authentication authentication = authenticationSupplier.get();

        // RequestAuthorizationContext에서 요청 객체를 가져와서 userId를 추출
        Long userId = extractUserIdFromRequest(context.getRequest());

        // WebSecurityService를 사용하여 권한 확인
        boolean granted = webSecurityService.checkUserId(authentication, userId);

        // 권한이 있으면 true, 없으면 false로 AuthorizationDecision 생성
        return new AuthorizationDecision(granted);
    }


}
