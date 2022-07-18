package com.godeltech.mastery.starterservice.annotation;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class OnStarterConditional extends AllNestedConditions {
    public OnStarterConditional() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(value="stats.where")
    public static class A{
    }
    @ConditionalOnProperty(value="stats.enable",havingValue = "true")
    public static class B{

    }
}
