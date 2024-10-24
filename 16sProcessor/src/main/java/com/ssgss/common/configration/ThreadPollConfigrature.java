package com.ssgss.common.configration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "threadpool")
@Getter
@Setter
public class ThreadPollConfigrature {
    private int downloadCores;
    private int denoiseCores;
    private int alphaCores;
    private int fastqcCores;
    private int importCores;
    private int fastqdumpCores;
    private int taxonomyCores;
    private int keepAliveTime;
}
