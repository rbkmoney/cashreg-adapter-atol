package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, Sno> SNO_MAP = new HashMap<>();

    static {
        for (Sno value : Sno.values()) {
            SNO_MAP.put(value.name(), value);
        }
    }

    public static Sno fromString(String value) {
        Sno sno = SNO_MAP.get(value);
        if (sno == null) {
            throw new IllegalArgumentException("No matching for [" + value + "]");
        }
        return sno;
    }

}
