package com.rbkmoney.adapter.atol.processor;


import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.utils.ErrorUtils;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.processor.Processor;
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

            AdapterState adapterState = entryStateModel.getState().getAdapterContext();
            adapterState.setReceiptId(response.getUuid());
            adapterState.setCashRegId(entryStateModel.getCashRegId());

            if (Step.CHECK_STATUS.equals(entryStateModel.getState().getAdapterContext().getNextStep())) {
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
            } else if (Step.CREATE.equals(entryStateModel.getState().getAdapterContext().getNextStep())) {
                adapterState.setNextStep(Step.CHECK_STATUS);
            }
            exitStateModel.setAdapterContext(adapterState);
            return exitStateModel;
        }

        return nextProcessor.process(response, entryStateModel);
    }

}