package com.godeltech.mastery.employeeservice.service;


import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface KafkaMessageSender {
    void send(EmployeeDtoResponse employeeDtoResponse) throws ExecutionException, InterruptedException, TimeoutException;
}
