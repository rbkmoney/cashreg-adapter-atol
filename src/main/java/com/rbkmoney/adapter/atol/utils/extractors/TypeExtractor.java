package com.rbkmoney.adapter.atol.utils.extractors;

import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.type.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeExtractor {

    private static final String UNKNOWN_TYPE = "unknown_type";

    public static String extractCashRegType(CashRegContext context) {
        return extractCashRegType(context.getSession().getType());
    }

    public static String extractCashRegType(Type type) {
        String state = UNKNOWN_TYPE;
        if (type.isSetCredit()) {
            state = Type._Fields.CREDIT.getFieldName();
        } else if (type.isSetDebit()) {
            state = Type._Fields.DEBIT.getFieldName();
        } else if (type.isSetRefundCredit()) {
            state = Type._Fields.REFUND_CREDIT.getFieldName();
        } else if (type.isSetRefundDebit()) {
            state = Type._Fields.REFUND_DEBIT.getFieldName();
        }
        return state.toUpperCase();
    }

}
