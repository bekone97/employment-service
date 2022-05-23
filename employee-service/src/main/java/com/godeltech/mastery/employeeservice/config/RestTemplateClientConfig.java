package com.godeltech.mastery.employeeservice.config;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.clients.rest.RestTemplateDepartmentClientImpl;
import com.godeltech.mastery.employeeservice.clients.rest.RestTemplateResponseErrorHandler;
import com.godeltech.mastery.employeeservice.interceptors.RestTemplateInterceptor;
import com.godeltech.mastery.employeeservice.security.service.SecurityServiceToken;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;

@ConditionalOnProperty(name = "client.department", havingValue = "rest")
@Configuration
@RequiredArgsConstructor
public class RestTemplateClientConfig {

    @Value("${client.department.base.url}")
    private String departmentHttpUrl;

    private final SecurityServiceToken securityServiceToken;

    @Bean
    public RestTemplate departmentRestTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(departmentHttpUrl));
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        var interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors))
            interceptors = new ArrayList<>();
        interceptors.add(new RestTemplateInterceptor(securityServiceToken));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public DepartmentApiClient departmentApiClient() {
        return new RestTemplateDepartmentClientImpl(departmentRestTemplate());
    }
}
