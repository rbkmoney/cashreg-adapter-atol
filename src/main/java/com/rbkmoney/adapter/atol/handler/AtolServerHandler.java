package com.rbkmoney.adapter.atol.handler;


import com.rbkmoney.adapter.atol.converter.entry.CtxToEntryModelConverter;
import com.rbkmoney.adapter.atol.converter.exit.ExitModelToProxyResultConverter;
import com.rbkmoney.adapter.atol.exception.UnsupportedMethodException;
import com.rbkmoney.adapter.atol.flow.StepResolver;
import com.rbkmoney.adapter.atol.handler.cashreg.CommonHandler;
import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.StateModel;
import com.rbkmoney.adapter.atol.model.Step;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.validator.CashRegContextValidator;
import com.rbkmoney.adapter.atol.validator.Validator;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.provider.CashRegProviderSrv;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class AtolServerHandler implements CashRegProviderSrv.Iface {

    private final CtxToEntryModelConverter ctxToEntryModelConverter;
    private final ExitModelToProxyResultConverter exitModelToProxyResultConverter;
    private final StepResolver stepResolver;
    private final CashRegContextValidator cashRegContextValidator;
    private final List<CommonHandler<ExitStateModel, CommonResponse, EntryStateModel>> handlers;

    @Override
    public CashRegResult register(CashRegContext context) throws TException {
        return handle(cashRegContextValidator, ctxToEntryModelConverter, exitModelToProxyResultConverter, context);
    }

    private <T, R> R handle(
            Validator<T> validator,
            Converter<T, EntryStateModel> entryConverter,
            Converter<ExitStateModel, R> exitConverter,
            T context
    ) throws TException {
        EntryStateModel entryStateModel = prepareEntryState(validator, entryConverter, context);
        ExitStateModel exitStateModel = handlers.stream()
                .filter(handler -> handler.isHandler(entryStateModel))
                .findFirst()
                .orElseThrow(UnsupportedMethodException::new)
                .handle(entryStateModel);
        exitStateModel.setNextStep(stepResolver.resolveExit(exitStateModel));
        return exitConverter.convert(exitStateModel);
    }

    private <T> EntryStateModel prepareEntryState(Validator<T> validator, Converter<T, EntryStateModel> entryConverter, T context) {
        validator.validate(context);
        EntryStateModel entryStateModel = entryConverter.convert(context);
        StateModel stateModel = entryStateModel.getStateModel();
        Step step = stepResolver.resolveEntry(stateModel);
        stateModel.setStep(step);
        return entryStateModel;
    }
}
