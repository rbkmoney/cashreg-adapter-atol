package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    /**
     * Return the enum constant of this type with the specified string value.
     *
     * @param code the string value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static Tax fromStringValue(String code) {
        for (Tax value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No matching for [" + code + "]");
    }


}
