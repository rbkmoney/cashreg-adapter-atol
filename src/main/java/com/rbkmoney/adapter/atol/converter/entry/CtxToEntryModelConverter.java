package com.rbkmoney.adapter.atol.converter.entry;


import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.TargetType;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashRegAdapterContextConveter;
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

@Component
@RequiredArgsConstructor
public class CtxToEntryModelConverter implements Converter<CashRegContext, EntryStateModel> {

    private final TargetTypeResolver resolveTargetType;
    private final CashRegAdapterContextConveter cashRegAdapterContextConveter;

    @Override
    public EntryStateModel convert(CashRegContext context) {
        AdapterContext adapterContext = cashRegAdapterContextConveter.convert(context);
        TargetType targetType = resolveTargetType.resolve(context.getSession().getType());

        Map<String, String> options = context.getOptions();
        OperationModel.OperationModelBuilder builder = OperationModel.builder();
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

        BigDecimal totalAmount = new BigDecimal(context.getSourceCreation().getPayment().getCash().getAmount())
                .movePointLeft(2);
        builder.total(totalAmount);

        List<Payments> paymentsList = new ArrayList<>();
        // TODO
        paymentsList.add(Payments.builder().sum(totalAmount).type(1).build());
        builder.payments(paymentsList);


        List<Vat> vatList = new ArrayList<>();
        // TODO
        builder.vats(vatList);

        OperationModel operationModel = builder.build();
        StateModel.StateModelBuilder stateModelBuilder = StateModel.builder()
                .targetType(targetType)
                .adapterContext(adapterContext);

                if(adapterContext != null && adapterContext.getNextStep() != null) {
                    stateModelBuilder.step(adapterContext.getNextStep());
                }

        return EntryStateModel.builder()
                .operationModel(operationModel)
                .stateModel(stateModelBuilder.build())
                .build();
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
                            .price(new BigDecimal(itemsLine.getPrice().getAmount()).divide(new BigDecimal(100)).setScale(2))
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
