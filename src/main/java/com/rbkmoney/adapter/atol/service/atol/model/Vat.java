package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vat {

    @JsonProperty
    private String type;

    /**
     * Сумма к оплате в рублях:
     * - целая часть не более 8 знаков;
     * - дробная часть не более 2 знаков
     */
    @JsonProperty
    private BigDecimal sum;

}
