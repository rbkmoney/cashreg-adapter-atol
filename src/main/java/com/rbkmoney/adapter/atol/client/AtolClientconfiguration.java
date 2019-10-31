package com.rbkmoney.adapter.atol.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.service.atol.AtolApi;
import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.converter.ip.ConverterIp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AtolClientconfiguration {

    @Bean
    AtolClient atolClient(ObjectMapper objectMapper, RestTemplate restTemplate, ConverterIp converterIp) {
        return new AtolClient(new AtolApi(restTemplate, objectMapper, converterIp));
    }

}
