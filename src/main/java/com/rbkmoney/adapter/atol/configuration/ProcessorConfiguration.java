package com.rbkmoney.adapter.atol.configuration;

import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.processor.ErrorProcessor;
import com.rbkmoney.adapter.atol.processor.Processor;
import com.rbkmoney.adapter.atol.processor.SuccessProcessor;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProcessorConfiguration {

    @Bean
    public Processor<ExitStateModel, EntryStateModel, CommonResponse> responseProcessorChain() {
        ErrorProcessor errorProcessor = new ErrorProcessor();
        return new SuccessProcessor(errorProcessor);
    }

}
