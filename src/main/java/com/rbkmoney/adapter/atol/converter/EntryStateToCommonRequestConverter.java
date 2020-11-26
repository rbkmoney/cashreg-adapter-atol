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
import com.rbkmoney.adapter.atol.utils.DateFormate;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.config.properties.AdapterCashregProperties;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField.*;
import static com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField.COMPANY_ADDRESS;


@Component
@RequiredArgsConstructor
public class EntryStateToCommonRequestConverter implements Converter<EntryStateModel, RequestWrapper<CommonRequest>> {

    private static final String DEFAULT_EMPTY_VALUE_TOKEN_API = "";

    private final VatsTransformer vatsTransformer;
    private final PaymentsTransformer paymentsTransformer;
    private final ItemsTransformer itemsTransformer;
    private final AdapterCashregProperties adapterCashRegProperties;

    @Override
    public RequestWrapper<CommonRequest> convert(EntryStateModel entryStateModel) {

        CommonRequest commonRequest = new CommonRequest();

        commonRequest.setExternalId(entryStateModel.getCashRegId());
        commonRequest.setTimestamp(DateFormate.getCurrentDate());

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
                        .payments(paymentsTransformer.transform(entryStateModel.getPayments()))
                        .items(itemsTransformer.transform(entryStateModel.getItems()))
                        .total(entryStateModel.getTotal())
                        .build());

        Service service = Service.builder().callbackUrl(entryStateModel.getCallbackUrl()).build();
        commonRequest.setService(service);

        Map<String, String> options = entryStateModel.getOptions();
        return new RequestWrapper<>(
                commonRequest,
                options.getOrDefault(URL.getField(), adapterCashRegProperties.getUrl()),
                options.get(GROUP.getField()),
                options.get(COMPANY_NAME.getField()),
                options.get(COMPANY_ADDRESS.getField()),
                DEFAULT_EMPTY_VALUE_TOKEN_API,
                options.get(OptionalField.LOGIN.getField()),
                options.get(OptionalField.PASS.getField())
        );
    }

}

