package com.godeltech.mastery.starterservice.config;

import com.godeltech.mastery.starterservice.annotation.ConditionOnPopap;
import com.godeltech.mastery.starterservice.annotation.ConditionalOnStarter;
import com.godeltech.mastery.starterservice.listener.ContextListener;
import com.godeltech.mastery.starterservice.listener.MyContextListener;
import com.godeltech.mastery.starterservice.properties.StarterProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(StarterProperties.class)
public class StarterConfiguration {

    @Bean
    @ConditionalOnProperty("stats.where")
    @Profile(value = "run")
    public MyContextListener myContextListener(StarterProperties starterProperties){
        return  new MyContextListener(starterProperties);
    }
    @Bean
    @ConditionOnPopap
    @ConditionalOnProperty("stats.where")
    @ConditionalOnMissingBean(value = MyContextListener.class)
    @ConditionalOnStarter
    public ContextListener contextListener(StarterProperties starterProperties){
        return new ContextListener(starterProperties);
    }




}
