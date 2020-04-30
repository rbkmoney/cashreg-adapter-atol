package com.rbkmoney.adapter.atol.configuration;

import com.rbkmoney.adapter.atol.handler.AtolServerHandler;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashregAdapterServiceLogDecorator;
import com.rbkmoney.damsel.cashreg.adapter.CashregAdapterSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    @Autowired
    public CashregAdapterSrv.Iface serverHandlerLogDecorator(AtolServerHandler atolServerHandler) {
        return new CashregAdapterServiceLogDecorator(atolServerHandler);
    }

}
