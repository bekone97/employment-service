package com.godeltech.mastery.employeeservice.config;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.clients.feign.FeignDepartmentClient;
import com.godeltech.mastery.employeeservice.clients.feign.FeignDepartmentClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "client.department",havingValue = "feign")
@Configuration
@EnableFeignClients(basePackages = {"com.godeltech.mastery.employeeservice.clients.feign"})
@RequiredArgsConstructor
public class FeignClientConfig {

    private final FeignDepartmentClient feignDepartmentClient;

    @Bean
    public DepartmentApiClient departmentApiClient(){
        return new FeignDepartmentClientImpl(feignDepartmentClient);
    }


}
