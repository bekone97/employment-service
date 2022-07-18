package com.godeltech.mastery.starterservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(value="stats")
public class StarterProperties {
    List<String> where;
    boolean enable;
}
