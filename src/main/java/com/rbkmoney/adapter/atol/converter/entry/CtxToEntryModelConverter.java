package com.rbkmoney.adapter.atol.converter.entry;


import com.rbkmoney.adapter.atol.service.atol.constant.PaymentType;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.TargetType;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashregAdapterContextConverter;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.flow.TargetTypeResolver;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.*;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import com.rbkmoney.damsel.cashreg.domain.PaymentInfo;
import com.rbkmoney.damsel.cashreg.domain.RussianLegalEntity;
import com.rbkmoney.damsel.cashreg.domain.TaxMode;
import com.rbkmoney.damsel.cashreg.receipt.Cart;
import com.rbkmoney.damsel.cashreg.receipt.ItemsLine;
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
public class CtxToEntryModelConverter implements Converter<CashregContext, EntryStateModel> {

    private final CashregAdapterContextConverter cashRegAdapterContextConveter;

    @Override
    public EntryStateModel convert(CashregContext context) {
        AdapterState adapterState = cashRegAdapterContextConveter.convert(context);

        Map<String, String> options = context.getOptions();
        EntryStateModel.EntryStateModelBuilder builder = EntryStateModel.builder();

        builder.options(options);
        builder.cashRegId(context.getCashregId());

        PaymentInfo paymentInfo = context.getSourceCreation().getPayment();
        builder.client(Client.builder()
                .email(paymentInfo.getEmail())
                .build()
        );

        RussianLegalEntity russianLegalEntity = context.getAccountInfo().getLegalEntity().getRussianLegalEntity();
        builder.company(Company.builder()
                .email(russianLegalEntity.getEmail())
                .inn(russianLegalEntity.getInn())
                .paymentAddress(russianLegalEntity.getActualAddress())
                .sno(TaxMode.findByValue(russianLegalEntity.getTaxMode().getValue()).name())
                .build()
        );

        builder.items(prepareCart(paymentInfo.getCart(), options));
        builder.total(prepareAmount(paymentInfo.getCash().getAmount()));

        List<ItemsLine> itemsLines = paymentInfo.getCart().getLines();
        builder.payments(itemsLines.stream().map(this::preparePayments).collect(Collectors.toList()));

        TargetType targetType = TargetTypeResolver.resolve(context.getSession().getType());
        StateModel.StateModelBuilder stateModelBuilder = StateModel.builder()
                .targetType(targetType);

        if (adapterState != null) {
            stateModelBuilder.adapterContext(adapterState);
        }

        builder.state(stateModelBuilder.build());
        return builder.build();
    }


    private Payments preparePayments(ItemsLine itemsLine) {
        BigDecimal fullPriceAmount = prepareAmount(itemsLine.getPrice().getAmount() * itemsLine.getQuantity());
        return Payments.builder().sum(fullPriceAmount).type(PaymentType.ELECTRONIC).build();
    }

    private BigDecimal prepareAmount(Long amount) {
        return new BigDecimal(amount).movePointLeft(2);
    }

    private List<Items> prepareCart(Cart cart, Map<String, String> options) {
        List<Items> itemsList = new ArrayList<>();
        cart.getLines().forEach(itemsLine -> {
            BigDecimal sum = prepareAmount(itemsLine.getPrice().getAmount() * itemsLine.getQuantity());
            itemsList.add(
                    Items.builder()
                            .quantity(new BigDecimal(itemsLine.getQuantity()).setScale(1))
                            .price(prepareAmount(itemsLine.getPrice().getAmount()))
                            .sum(sum)
                            .vat(Vat.builder().type(
                                    com.rbkmoney.adapter.atol.service.atol.constant.Vat.codeTextOf(
                                            itemsLine.getTax()).getCode())
                                    .build())
                            .paymentMethod(options.get(OptionalField.PAYMENT_METHOD.getField()))
                            .paymentObject(options.get(OptionalField.PAYMENT_OBJECT.getField()))
                            .name(itemsLine.getProduct())
                            .build()
            );
        });

        return itemsList;
    }

}
