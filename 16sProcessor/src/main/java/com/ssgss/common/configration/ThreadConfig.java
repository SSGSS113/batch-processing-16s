package com.ssgss.common.configration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "thread")
@Getter
@Setter
public class ThreadConfig {
    private int denoise;
    private int taxonomy;
}
