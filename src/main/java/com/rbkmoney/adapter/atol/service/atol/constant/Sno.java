package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Система налогообложения
 */
@Getter
@RequiredArgsConstructor
public enum Sno {

    TOTAL_SN("osn", "общая СН"),
    SIMPLIFIED_SN_INCOME("usn_income", "упрощенная СН (доходы)"),
    SIMPLIFIED_SN_INCOME_MINUS_COSTS("usn_income_outcome", "упрощенная СН (доходы минус расходы)"),
    SINGLE_TAX_ON_IMPUTED_INCOME("envd", "единый налог на вмененный доход"),
    SINGLE_AGRICULTURAL_TAX("esn", "единый сельскохозяйственный налог"),
    PATENT_SN("patent", "патентная СН");

    private final String code;
    private final String message;


    /**
     * Return the enum constant of this type with the specified string value.
     *
     * @param code the string value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static Sno fromStringValue(String code) {
        for (Sno value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No matching for [" + code + "]");
    }


}
