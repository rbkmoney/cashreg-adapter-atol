package com.rbkmoney.adapter.atol.converter.exit;


import com.rbkmoney.adapter.atol.configuration.TimerProperties;
import com.rbkmoney.adapter.atol.constant.OptionalField;
import com.rbkmoney.adapter.atol.model.AdapterContext;
import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.Step;
import com.rbkmoney.adapter.atol.serializer.AdapterSerializer;
import com.rbkmoney.adapter.atol.utils.wrapper.cashreg.creators.CashRegProviderCreators;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import com.rbkmoney.damsel.cashreg.provider.Intent;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.error.mapping.ErrorMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

import static java.lang.Integer.parseInt;
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

        CashRegResult cashRegResult = new CashRegResult()
                .setIntent(intent)
                .setState(serializer.writeByte(entryStateModel.getStateModel().getAdapterContext()));

        if (exitStateModel.getCashRegInfo() != null) {
            entryStateModel.getStateModel().setStep(Step.CHECK_STATUS);
            cashRegResult.setCashregInfo(exitStateModel.getCashRegInfo());
        }

        return cashRegResult;
    }

    public static Integer extractPollingDelay(Map<String, String> options, int pollingDelay) {
        return parseInt(options.getOrDefault(OptionalField.POLLING_DELAY.getField(), String.valueOf(pollingDelay)));
    }

    public static Integer extractMaxTimePolling(Map<String, String> options, int maxTimePolling) {
        return Integer.parseInt(options.getOrDefault(OptionalField.MAX_TIME_POLLING.getField(), String.valueOf(maxTimePolling)));
    }

}
