package com.godeltech.mastery.employeeservice.interceptors;

import com.godeltech.mastery.employeeservice.security.service.SecurityServiceToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final SecurityServiceToken securityServiceToken;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().setBearerAuth(securityServiceToken.getToken());
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }

}
