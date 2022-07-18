package com.godeltech.mastery.starterservice.initializer;

import lombok.var;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.Stream;

public class StarterInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var profiles =applicationContext.getEnvironment().getActiveProfiles();
        if (profiles.length==0)
            applicationContext.getEnvironment().setActiveProfiles("test");
    }
}
