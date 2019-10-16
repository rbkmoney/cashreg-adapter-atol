package com.rbkmoney.adapter.atol.configuration;

import com.rbkmoney.adapter.atol.converter.CashRegAdapterServiceLogDecorator;
import com.rbkmoney.adapter.atol.handler.AtolServerHandler;
import com.rbkmoney.damsel.cashreg.provider.CashRegProviderSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    @Autowired
    public CashRegProviderSrv.Iface serverHandlerLogDecorator(AtolServerHandler atolServerHandler) {
        return new CashRegAdapterServiceLogDecorator(atolServerHandler);
    }

}
