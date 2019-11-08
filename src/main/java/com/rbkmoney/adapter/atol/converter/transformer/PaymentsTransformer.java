package com.rbkmoney.adapter.atol.converter.transformer;

import com.rbkmoney.adapter.atol.service.atol.model.Payments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentsTransformer {

    public List<Payments> transform(List<com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Payments> vatList) {
        return vatList.stream().map(this::toPayment).collect(Collectors.toList());
    }

    private Payments toPayment(com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Payments payment) {
        return Payments.builder().sum(payment.getSum()).type(payment.getType()).build();
    }

}
