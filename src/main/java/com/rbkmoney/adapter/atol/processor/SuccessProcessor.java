package com.rbkmoney.adapter.atol.processor;

import com.rbkmoney.adapter.atol.model.AdapterContext;
import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.Step;
import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.utils.ErrorUtils;
import com.rbkmoney.damsel.cashreg.CashRegInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SuccessProcessor implements Processor<ExitStateModel, EntryStateModel, CommonResponse> {

    private final Processor<ExitStateModel, EntryStateModel, CommonResponse> nextProcessor;

    @Override
    public ExitStateModel process(CommonResponse response, EntryStateModel entryStateModel) {
        if (!ErrorUtils.hasError(response)) {
            ExitStateModel exitStateModel = new ExitStateModel();
            exitStateModel.setEntryStateModel(entryStateModel);

            AdapterContext adapterContext = entryStateModel.getStateModel().getAdapterContext();
            adapterContext.setReceiptId(response.getUuid());
            adapterContext.setCashRegId(entryStateModel.getOperationModel().getCashRegId());

            if (Step.CHECK_STATUS.equals(exitStateModel.getNextStep())) {
                if (Status.DONE.getValue().equalsIgnoreCase(response.getStatus())) {
                    CashRegInfo cashRegInfo = new CashRegInfo();
                    cashRegInfo.setReceiptId(response.getUuid());
                    cashRegInfo.setCallbackUrl(response.getCallbackUrl());
                    cashRegInfo.setDaemonCode(response.getDaemonCode());
                    cashRegInfo.setDeviceCode(response.getDeviceCode());
                    cashRegInfo.setGroupCode(response.getGroupCode());
                    cashRegInfo.setTimestamp(response.getTimestamp());
                    exitStateModel.setCashRegInfo(cashRegInfo);
                }
            } else if (Step.CREATE.equals(exitStateModel.getNextStep())) {
                exitStateModel.setNextStep(Step.CHECK_STATUS);
                adapterContext.setNextStep(Step.CHECK_STATUS);
            }
            exitStateModel.setAdapterContext(adapterContext);
            return exitStateModel;
        }

        return nextProcessor.process(response, entryStateModel);
    }

}