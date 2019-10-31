package com.rbkmoney.adapter.atol.converter.exit;


import com.rbkmoney.adapter.cashreg.spring.boot.starter.config.properties.TimerProperties;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterContext;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.serializer.AdapterSerializer;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.creators.CashRegProviderCreators;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import com.rbkmoney.damsel.cashreg.provider.Intent;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.error.mapping.ErrorMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

import static com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.extractors.OptionsExtractors.extractMaxTimePolling;
import static com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.extractors.OptionsExtractors.extractPollingDelay;
import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@RequiredArgsConstructor
public class ExitModelToProxyResultConverter implements Converter<ExitStateModel, CashRegResult> {

    private final ErrorMapping errorMapping;
    private final TimerProperties timerProperties;
    private final AdapterSerializer serializer;

    @Override
    public CashRegResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        AdapterContext adapterContext = entryStateModel.getStateModel().getAdapterContext();
        if (adapterContext.getMaxDateTimePolling() == null) {
            int timerMaxTimePolling = extractMaxTimePolling(entryStateModel.getOperationModel().getOptions(), timerProperties.getMaxTimePolling());
            Instant maxDateTime = Instant.now().plus(timerMaxTimePolling, MINUTES);
            adapterContext.setMaxDateTimePolling(maxDateTime);
        }

        Intent intent = CashRegProviderCreators.createFinishIntentSuccess();
        if (exitStateModel.getErrorCode() != null) {
            Failure failure = errorMapping.getFailureByCodeAndDescription(
                    exitStateModel.getErrorCode(),
                    exitStateModel.getErrorMessage()
            );
            intent = CashRegProviderCreators.createFinishIntentFailure(failure);
        }

        if (exitStateModel.getCashRegInfo() == null) {
            Map<String, String> options = entryStateModel.getOperationModel().getOptions();
            intent = CashRegProviderCreators.createIntentWithSleepIntent(extractPollingDelay(options, timerProperties.getPollingDelay()));
        }

        if (Step.CREATE.equals(adapterContext.getNextStep())) {
            adapterContext.setNextStep(Step.CHECK_STATUS);
        }

        CashRegResult cashRegResult = new CashRegResult()
                .setIntent(intent)
                .setState(serializer.writeByte(adapterContext));

        if (exitStateModel.getCashRegInfo() != null) {
            entryStateModel.getStateModel().setStep(Step.CHECK_STATUS);
            cashRegResult.setCashregInfo(exitStateModel.getCashRegInfo());
        }

        return cashRegResult;
    }

}
