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
import com.rbkmoney.adapter.cashreg.spring.boot.starter.config.properties.AdapterCashRegProperties;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
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
    private final AdapterCashRegProperties adapterCashRegProperties;

    @Override
    public RequestWrapper<CommonRequest> convert(EntryStateModel entryStateModel) {

        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setLogin(entryStateModel.getAuth().getLogin());
        commonRequest.setPass(entryStateModel.getAuth().getPass());

        commonRequest.setExternalId(entryStateModel.getCashRegId());

        commonRequest.setReceipt(
                Receipt.builder()
                        .client(
                                Client.builder()
                                        .email(entryStateModel.getClient().getEmail())
                                        .build()
                        )
                        .company(
                                Company.builder()
                                        .inn(entryStateModel.getCompany().getInn())
                                        .email(entryStateModel.getCompany().getEmail())
                                        .paymentAddress(entryStateModel.getCompany().getPaymentAddress())
                                        .sno(entryStateModel.getCompany().getSno())
                                        .build()
                        )
                        .payments(paymentsTransformer.transform(entryStateModel.getPayments())) //
                        .items(itemsTransformer.transform(entryStateModel.getItems()))
                        .total(entryStateModel.getTotal()) //
                        .vats(vatsTransformer.transform(entryStateModel.getVats())) //
                        .build());

        Service service = Service.builder().callbackUrl(entryStateModel.getCallbackUrl()).build();
        commonRequest.setService(service);

        return new RequestWrapper<>(
                commonRequest,
                entryStateModel.getOptions().getOrDefault(OptionalField.URL.getField(), adapterCashRegProperties.getUrl()),
                entryStateModel.getOptions().get(OptionalField.GROUP.getField()),
                entryStateModel.getOptions().get(OptionalField.COMPANY_NAME.getField()),
                entryStateModel.getOptions().get(OptionalField.COMPANY_ADDRESS.getField()),
                DEFAULT_EMPTY_VALUE_TOKEN_API
        );
    }

}

