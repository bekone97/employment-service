package com.godeltech.mastery.employeeservice.clients.rest;

import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().series()== CLIENT_ERROR
        ||response.getStatusCode().series()== SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode().series()){
            case SERVER_ERROR:
                log.error("There was SERVER_ERROR ");
                throw new ResourceNotFoundException("There was a server error");
            case CLIENT_ERROR:
                log.error("There was CLIENT_ERROR ");
                throw new ResourceNotFoundException("There was a client error ");
            default:
                log.error("There was unexpected ERROR");
                throw new ResourceNotFoundException("Unexpected exception with client");
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode().series()){
            case SERVER_ERROR:
                log.error("There was SERVER_ERROR by url {}",url);
                throw new ResourceNotFoundException("There was a server error by url:"+url);
            case CLIENT_ERROR:
                log.error("There was CLIENT_ERROR by url {}:",url);
                throw new ResourceNotFoundException("There was a client error by url:" +url);
            default:
                log.error("There was unexpected ERROR by url {}:",url);
                throw new ResourceNotFoundException("An unexpected exception with client by url "+url);
        }
    }

}
