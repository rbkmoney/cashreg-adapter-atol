package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.NoArgsConstructor;

/**
 * Операции с фискальным аппаратом
 */
@NoArgsConstructor
public class Operations {

    /**
     * чек «Приход»
     */
    public static final String SELL = "sell";

    /**
     * чек «Возврат прихода»
     */
    public static final String SELL_REFUND = "sell_refund";

    /**
     * чек «Коррекция прихода»
     */
    public static final String SELL_CORRECTION = "sell_correction";

    /**
     * чек «Расход»
     */
    public static final String BUY = "buy";

    /**
     * чек «Возврат расхода»
     */
    public static final String BUY_REFUND = "buy_refund";

    /**
     * чек «Коррекция расхода»
     */
    public static final String BUY_CORRECTION = "buy_correction";

}
