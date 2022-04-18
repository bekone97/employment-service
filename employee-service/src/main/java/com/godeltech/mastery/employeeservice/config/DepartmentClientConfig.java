package com.godeltech.mastery.employeeservice.config;

import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@EnableFeignClients(basePackages = {"com.godeltech.mastery.employeeservice.clients.feign"})
public class DepartmentClientConfig {
    @Value("${client.department.base.url}")
    private String departmentHttpUrl;
    @Bean
    public RestTemplate restTemplate(){
        var restTemplate= new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(departmentHttpUrl));
        return restTemplate;
    }
}
