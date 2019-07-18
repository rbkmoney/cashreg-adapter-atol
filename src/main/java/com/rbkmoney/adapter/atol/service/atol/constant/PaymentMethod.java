package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    FULL_PREPAYMENT("full_prepayment", "предоплата 100%. Полная предварительная оплата до момента передачи предмета расчета"),
    PREPAYMENT("prepayment", " предоплата. Частичная предварительная оплата до момента передачи предмета расчета"),
    ADVANCE("advance", "аванс"),
    FULL_PAYMENT("full_payment", "полный расчет. Полная оплата, в том числе с учетом аванса (предварительной оплаты) в момент передачи предмета расчета."),
    PARTIAL_PAYMENT("partial_payment", "частичный расчет и кредит. Частичная оплата предмета расчета в момент его передачи с последующей оплатой в кредит"),
    CREDIT("credit", "передача в кредит. Передача предмета расчета без его оплаты в момент его передачи с последующей оплатой в кредит."),
    CREDIT_PAYMENT("credit_payment", " оплата кредита. Оплата предмета расчета после его передачи с оплатой в кредит (оплата кредита)");

    private final String command;
    private final String description;

}
