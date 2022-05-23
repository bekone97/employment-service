package com.godeltech.mastery.employeeservice.clients.feign;

import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
public class FeignDepartmentClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        if (response.status() == NOT_FOUND.value()) {
            log.error("Department was not found");
            return new ResourceNotFoundException("Department was not found by url:" + response.request().url());
        }
        return defaultErrorDecoder.decode(s, response);
    }

}
