package com.rbkmoney.adapter.atol.processor;

import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.Step;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.utils.ErrorUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SuccessProcessor implements Processor<ExitStateModel, EntryStateModel, CommonResponse> {

    private final Processor<ExitStateModel, EntryStateModel, CommonResponse> nextProcessor;

    @Override
    public ExitStateModel process(CommonResponse response, EntryStateModel entryStateModel) {
        if (!ErrorUtils.hasError(response)) {
            ExitStateModel exitStateModel = new ExitStateModel();
            exitStateModel.setEntryStateModel(entryStateModel);
            if (Step.CREATE.equals(exitStateModel.getNextStep())) {
                exitStateModel.setNextStep(Step.CHECK_STATUS);
            }
            return exitStateModel;
        } else if (nextProcessor != null) {
            return nextProcessor.process(response, entryStateModel);
        }
        return null;
    }

}