package com.rbkmoney.adapter.atol.converter.transformer;

import com.rbkmoney.adapter.atol.service.atol.model.Vat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VatsTransformer {

    public List<Vat> transform(List<com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Vat> vatList) {
        return vatList.stream().map(this::toVat).collect(Collectors.toList());
    }

    private Vat toVat(com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Vat vat) {
        return Vat.builder().sum(vat.getSum()).type(vat.getType()).build();
    }

}
