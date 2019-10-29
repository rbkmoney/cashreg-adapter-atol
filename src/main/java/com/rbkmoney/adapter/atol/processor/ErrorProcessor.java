package com.rbkmoney.adapter.atol.processor;


import com.rbkmoney.adapter.atol.model.AdapterContext;
import com.rbkmoney.adapter.atol.model.CustomError;
import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.utils.ErrorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class ErrorProcessor implements Processor<ExitStateModel, EntryStateModel, CommonResponse> {

    @Override
    public ExitStateModel process(CommonResponse response, EntryStateModel entryStateModel) {
        Instant currentTime = Instant.now();
        AdapterContext adapterContext = new AdapterContext();
        if (entryStateModel.getStateModel().getAdapterContext() != null) {
            adapterContext = entryStateModel.getStateModel().getAdapterContext();
        }

        ExitStateModel exitStateModel = new ExitStateModel();
        if (ErrorUtils.hasError(response)) {
            exitStateModel.setEntryStateModel(entryStateModel);
        } else if (adapterContext.getMaxDateTimePolling().getEpochSecond() < currentTime.getEpochSecond()) {
            log.error("Sleep Timeout for response: {}!", response);
            exitStateModel.setErrorCode(CustomError.SLEEP_TIMEOUT.getCode());
            exitStateModel.setErrorMessage(CustomError.SLEEP_TIMEOUT.getMessage());

        } else {
            log.error("Unknown result code for response: {}!", response);
            exitStateModel.setErrorCode(CustomError.UNKNOWN.getCode());
            exitStateModel.setErrorMessage(CustomError.UNKNOWN.getMessage());
        }
        return exitStateModel;
    }

}
