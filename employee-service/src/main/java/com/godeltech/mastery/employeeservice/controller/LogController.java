package com.godeltech.mastery.employeeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger("logController");

    @GetMapping
    public String testActuator() {
        logger.debug("Debug level log message");
        logger.info("Info level log message");
        logger.warn("Warn level log message");
        logger.error("Error level log message");
        return "test actuator";
    }
}
