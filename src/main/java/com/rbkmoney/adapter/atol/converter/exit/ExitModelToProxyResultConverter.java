package com.rbkmoney.adapter.atol.converter.exit;


import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.service.IntentService;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.state.serializer.AdapterSerializer;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.creators.CashregAdapterCreators;
import com.rbkmoney.adapter.common.model.PollingInfo;
import com.rbkmoney.damsel.cashreg.adapter.CashregResult;
import com.rbkmoney.damsel.cashreg.adapter.Intent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExitModelToProxyResultConverter implements Converter<ExitStateModel, CashregResult> {

    private final AdapterSerializer serializer;
    private final IntentService intentService;

    @Override
    public CashregResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        AdapterState adapterState = entryStateModel.getState().getAdapterContext();

        log.info("ExitStateModel convert start {} ", exitStateModel);

        PollingInfo pollingInfo = new PollingInfo();
        if (adapterState.getPollingInfo() == null) {
            pollingInfo.setStartDateTimePolling(Instant.now());
            pollingInfo.setMaxDateTimePolling(intentService.extractMaxDateTimeInstant(entryStateModel));
            adapterState.setPollingInfo(pollingInfo);
            exitStateModel.getAdapterContext().setPollingInfo(adapterState.getPollingInfo());
        }
        log.info("ExitStateModel convert start, polling set {} ", exitStateModel);

        Intent intent = CashregAdapterCreators.createFinishIntentSuccess();
        if (exitStateModel.getErrorCode() != null) {
            intent = intentService.getFailureByCodeAndDesc(exitStateModel);
            return new CashregResult().setIntent(intent).setState(serializer.writeByte(adapterState));
        }

        if (exitStateModel.getInfo() == null) {
            intent = intentService.getSleep(exitStateModel);
        }

        if (Step.CREATE == adapterState.getNextStep()) {
            adapterState.setNextStep(Step.CHECK_STATUS);
        }

        CashregResult cashRegResult = new CashregResult()
                .setIntent(intent)
                .setState(serializer.writeByte(adapterState));

        if (exitStateModel.getInfo() != null) {
            entryStateModel.getState().getAdapterContext().setNextStep(Step.CHECK_STATUS);
            cashRegResult.setInfo(exitStateModel.getInfo());
        }

        return cashRegResult;
    }

}