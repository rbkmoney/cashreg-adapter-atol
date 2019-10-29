package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Устанавливает номер налога в ККТ
 */
@Getter
@RequiredArgsConstructor
public enum Tax {

    NONE("none", "без НДС"),
    VAT0("vat0", "НДС по ставке 0%"),
    VAT10("vat10", "НДС чека по ставке 10%"),
    VAT18("vat18", "НДС чека по ставке 18%"),
    VAT110("vat110", "НДС чека по расчетной ставке 10/110"),
    VAT118("vat118", "НДС чека по расчетной ставке 18/118"),
    VAT20("vat20", "НДС чека по ставке 20%"),
    VAT120("vat120", "НДС чека по расчетной ставке 20/120"),
    ;

    private final String code;
    private final String message;

    private static final Map<String, Tax> TAX_MAP = new HashMap<>();

    static {
        for (Tax value : Tax.values()) {
            TAX_MAP.put(value.name(), value);
        }
    }

    public static Tax fromString(String value) {
        Tax tax = TAX_MAP.get(value);
        if (tax == null) {
            throw new IllegalArgumentException("No matching for [" + value + "]");
        }
        return tax;
    }


}
