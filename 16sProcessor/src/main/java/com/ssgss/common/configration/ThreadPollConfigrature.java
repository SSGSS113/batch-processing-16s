package com.ssgss.common.configration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
