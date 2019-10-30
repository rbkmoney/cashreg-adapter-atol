package com.rbkmoney.adapter.atol.converter.transformer;

import com.rbkmoney.adapter.atol.service.atol.model.Items;
import com.rbkmoney.adapter.atol.service.atol.model.Vat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemsTransformer {

    public List<Items> transform(List<com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Items> itemsList) {
        return itemsList.stream().map(this::toVat).collect(Collectors.toList());
    }

    private Items toVat(com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Items item) {
        return Items.builder()
                .sum(item.getSum())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .vat(Vat.builder().sum(item.getVat().getSum()).type(item.getVat().getType()).build())
                .measurementUnit(item.getMeasurementUnit())
                .paymentMethod(item.getPaymentMethod())
                .paymentObject(item.getPaymentObject())
                .build();
    }

}
