package com.rbkmoney.adapter.atol.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.configuration.properties.AtolProperties;
import com.rbkmoney.adapter.atol.service.atol.AtolApi;
import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.atol.utils.ConverterIp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AtolProperties.class)
public class AtolClientconfiguration {

    @Bean
    //@RequestScope
    AtolClient atolClient(
            ObjectMapper objectMapper,
            RestTemplate restTemplate,
            AtolProperties atolProperties
    ) {
        AtolApi atolApi = new AtolApi(restTemplate, objectMapper, new ConverterIp(), atolProperties);
        return new AtolClient(atolApi, atolProperties);
    }


}
