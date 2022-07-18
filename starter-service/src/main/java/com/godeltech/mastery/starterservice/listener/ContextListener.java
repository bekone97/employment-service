package com.godeltech.mastery.starterservice.listener;

import com.godeltech.mastery.starterservice.properties.StarterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
@RequiredArgsConstructor
public class ContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private final StarterProperties starterProperties;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        starterProperties.getWhere()
                        .forEach(System.out::println);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
