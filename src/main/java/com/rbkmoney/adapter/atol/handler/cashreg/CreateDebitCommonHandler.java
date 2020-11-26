package com.rbkmoney.adapter.atol.handler.cashreg;

import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.request.RequestWrapper;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.TargetType;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.handler.CommonHandlerImpl;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.processor.Processor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateDebitCommonHandler extends CommonHandlerImpl<ExitStateModel, RequestWrapper<CommonRequest>, CommonResponse, EntryStateModel> {

    public CreateDebitCommonHandler(
            AtolClient client,
            Converter<EntryStateModel, RequestWrapper<CommonRequest>> converter,
            Processor<ExitStateModel, EntryStateModel, CommonResponse> responseProcessorChain
    ) {
        super(client::debit, converter, responseProcessorChain);
    }

    @Override
    public boolean isHandler(EntryStateModel entryStateModel) {
        return Step.CREATE.equals(entryStateModel.getState().getAdapterContext().getNextStep())
                && TargetType.DEBIT.equals(entryStateModel.getState().getTargetType());
    }

}
