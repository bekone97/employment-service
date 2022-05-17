package com.godeltech.mastery.employeeservice.interceptors;

import com.godeltech.mastery.employeeservice.security.service.SecurityServiceToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

    private final SecurityServiceToken securityServiceToken;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var token = securityServiceToken.getToken();
        requestTemplate.header(AUTHORIZATION,"Bearer "+token);
    }
}
