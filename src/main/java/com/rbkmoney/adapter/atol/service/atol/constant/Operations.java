package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Операции с фискальным аппаратом
 */
@Getter
@AllArgsConstructor
public enum  Operations {

    // чек «Приход»
    SELL("sell"),
    // чек «Возврат прихода»
    SELL_REFUND("sell_refund"),
    // чек «Коррекция прихода»
    SELL_CORRECTION("sell_correction"),
    // чек «Расход»
    BUY("buy"),
    // чек «Возврат расхода»
    BUY_REFUND("buy_refund"),
    // чек «Коррекция расхода»
    BUY_CORRECTION("buy_correction");

    private String operation;

}
