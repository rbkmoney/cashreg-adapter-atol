package com.rbkmoney.adapter.atol.converter;


import com.rbkmoney.adapter.atol.converter.transformer.ItemsTransformer;
import com.rbkmoney.adapter.atol.converter.transformer.PaymentsTransformer;
import com.rbkmoney.adapter.atol.converter.transformer.VatsTransformer;
import com.rbkmoney.adapter.atol.service.atol.model.Client;
import com.rbkmoney.adapter.atol.service.atol.model.Company;
import com.rbkmoney.adapter.atol.service.atol.model.Receipt;
import com.rbkmoney.adapter.atol.service.atol.model.Service;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.request.RequestWrapper;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.OperationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EntryStateToCommonRequestConverter implements Converter<EntryStateModel, RequestWrapper<CommonRequest>> {

    private static final String DEFAULT_EMPTY_VALUE_TOKEN_API = "";

    private final VatsTransformer vatsTransformer;
    private final PaymentsTransformer paymentsTransformer;
    private final ItemsTransformer itemsTransformer;

    @Override
    public RequestWrapper<CommonRequest> convert(EntryStateModel entryStateModel) {
        OperationModel operationModel = entryStateModel.getOperationModel();
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setLogin(operationModel.getAuth().getLogin());
        commonRequest.setPass(operationModel.getAuth().getPass());

        commonRequest.setExternalId(operationModel.getCashRegId());

        commonRequest.setReceipt(
                Receipt.builder()
                        .client(
                                Client.builder()
                                        .email(operationModel.getClient().getEmail())
                                        .build()
                        )
                        .company(
                                Company.builder()
                                        .inn(operationModel.getCompany().getInn())
                                        .email(operationModel.getCompany().getEmail())
                                        .paymentAddress(operationModel.getCompany().getPaymentAddress())
                                        .sno(operationModel.getCompany().getSno())
                                        .build()
                        )
                        .payments(paymentsTransformer.transform(operationModel.getPayments())) //
                        .items(itemsTransformer.transform(operationModel.getItems()))
                        .total(operationModel.getTotal()) //
                        .vats(vatsTransformer.transform(operationModel.getVats())) //
                        .build());

        Service service = Service.builder().callbackUrl(operationModel.getCallbackUrl()).build();
        commonRequest.setService(service);

        return new RequestWrapper<>(
                commonRequest,
                operationModel.getOptions().get(OptionalField.URL.getField()),
                operationModel.getOptions().get(OptionalField.GROUP.getField()),
                operationModel.getOptions().get(OptionalField.COMPANY_NAME.getField()),
                operationModel.getOptions().get(OptionalField.COMPANY_ADDRESS.getField()),
                DEFAULT_EMPTY_VALUE_TOKEN_API
        );
    }

}

