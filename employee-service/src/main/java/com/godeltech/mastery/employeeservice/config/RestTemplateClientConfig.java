package com.godeltech.mastery.employeeservice.config;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.clients.rest.RestTemplateDepartmentClientImpl;
import com.godeltech.mastery.employeeservice.clients.rest.RestTemplateResponseErrorHandler;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ConditionalOnProperty(name="client.department",havingValue = "rest")
@Configuration
public class RestTemplateClientConfig {

    @Value("${client.department.base.url}")
    private String departmentHttpUrl;

    @Bean
    public RestTemplate departmentRestTemplate(){
        var restTemplate= new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(departmentHttpUrl));
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    public DepartmentApiClient departmentApiClient(){
        return new RestTemplateDepartmentClientImpl(departmentRestTemplate());
    }
}
