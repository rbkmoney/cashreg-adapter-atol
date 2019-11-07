package com.rbkmoney.adapter.atol.converter.entry;


import com.rbkmoney.adapter.atol.service.atol.constant.PaymentType;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.TargetType;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashRegAdapterContextConverter;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.flow.TargetTypeResolver;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.*;
import com.rbkmoney.damsel.cashreg.Cart;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg_domain.RussianLegalEntity;
import com.rbkmoney.damsel.cashreg_domain.TaxMode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CtxToEntryModelConverter implements Converter<CashRegContext, EntryStateModel> {

    private final CashRegAdapterContextConverter cashRegAdapterContextConveter;

    @Override
    public EntryStateModel convert(CashRegContext context) {
        AdapterState adapterState = cashRegAdapterContextConveter.convert(context);

        Map<String, String> options = context.getOptions();
        EntryStateModel.EntryStateModelBuilder builder = EntryStateModel.builder();

        builder.options(context.getOptions());
        builder.cashRegId(context.getCashregId());

        Auth auth = Auth.builder()
                .login(options.get(OptionalField.LOGIN.getField()))
                .pass(options.get(OptionalField.PASS.getField()))
                .build();
        builder.auth(auth);

        Client client = Client.builder()
                .email(context.getSourceCreation().getPayment().getEmail())
                .build();
        builder.client(client);

        RussianLegalEntity russianLegalEntity = context.getAccountInfo().getLegalEntity().getRussianLegalEntity();
        Company company = Company.builder()
                .email(russianLegalEntity.getEmail())
                .inn(russianLegalEntity.getInn())
                .paymentAddress(russianLegalEntity.getActualAddress())
                .sno(TaxMode.findByValue(russianLegalEntity.getTaxMode().getValue()).name())
                .build();
        builder.company(company);

        builder.items(prepareCart(context.getSourceCreation().getPayment().getCart(), options));

        BigDecimal totalAmount = prepareAmount(context.getSourceCreation().getPayment().getCash().getAmount());
        builder.total(totalAmount);

        List<Payments> paymentsList = context.getSourceCreation().getPayment().getCart().getLines().stream().map(itemsLine -> {
                    BigDecimal fullPriceAmount = new BigDecimal(itemsLine.getPrice().getAmount()).multiply(new BigDecimal(itemsLine.getQuantity())).movePointLeft(2);
                    return Payments.builder().sum(fullPriceAmount).type(PaymentType.ELECTRONIC).build();
                }
        ).collect(Collectors.toList());
        builder.payments(paymentsList);


        List<Vat> vatList = context.getSourceCreation().getPayment().getCart().getLines().stream().map(itemsLine -> Vat.builder()
                .sum(prepareAmount(itemsLine.getPrice().getAmount()))
                .type(itemsLine.getTax())
                .build()).collect(Collectors.toList());
        builder.vats(vatList);

        TargetType targetType = TargetTypeResolver.resolve(context.getSession().getType());
        StateModel.StateModelBuilder stateModelBuilder = StateModel.builder()
                .targetType(targetType);

        if (adapterState != null) {
            stateModelBuilder.adapterContext(adapterState);
        }

        builder.state(stateModelBuilder.build());
        return builder.build();
    }

    private BigDecimal prepareAmount(Long amount) {
        return new BigDecimal(amount).movePointLeft(2);
    }

    private List<Items> prepareCart(Cart cart, Map<String, String> options) {
        List<Items> itemsList = new ArrayList<>();
        cart.getLines().forEach(itemsLine -> {

            BigDecimal sum = new BigDecimal(itemsLine.getPrice().getAmount())
                    .multiply(new BigDecimal(itemsLine.getQuantity()))
                    .movePointLeft(2);

            itemsList.add(
                    Items.builder()
                            .quantity(new BigDecimal(itemsLine.getQuantity()).setScale(1))
                            .price(prepareAmount(itemsLine.getPrice().getAmount()))
                            .sum(sum)
                            .vat(Vat.builder().type(itemsLine.getTax()).build())
                            .paymentMethod(options.get(OptionalField.PAYMENT_METHOD.getField()))
                            .paymentObject(options.get(OptionalField.PAYMENT_OBJECT.getField()))
                            .name(itemsLine.getProduct())
                            .build()
            );
        });

        return itemsList;
    }

}
