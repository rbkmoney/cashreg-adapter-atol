package com.rbkmoney.adapter.atol.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties("time.config")
@Validated
@Getter
@Setter
public class TimerProperties {

    @NotNull
    private int maxTimePolling;

    @NotNull
    private int pollingDelay;

}
