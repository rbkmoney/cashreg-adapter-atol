package com.rbkmoney.adapter.atol.handler.cashreg;

import com.rbkmoney.adapter.atol.constant.TargetType;
import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.Step;
import com.rbkmoney.adapter.atol.processor.Processor;
import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.request.RequestWrapper;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class CreateRefundCreditCommonHandler extends CommonHandlerImpl<ExitStateModel, RequestWrapper<CommonRequest>, CommonResponse, EntryStateModel> {

    public CreateRefundCreditCommonHandler(
            AtolClient client,
            Converter<EntryStateModel, RequestWrapper<CommonRequest>> converter,
            Processor<ExitStateModel, EntryStateModel, CommonResponse> responseProcessorChain
    ) {
        super(client::refundCredit, converter, responseProcessorChain);
    }

    @Override
    public boolean isHandler(EntryStateModel entryStateModel) {
        return Step.CREATE.equals(entryStateModel.getStateModel().getStep())
                && TargetType.REFUND_CREDIT.equals(entryStateModel.getStateModel().getTargetType());
    }

}