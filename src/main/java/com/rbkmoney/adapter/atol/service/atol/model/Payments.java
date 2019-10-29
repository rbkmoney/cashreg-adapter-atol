package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Оплата. Ограничение по количеству от 1 до 10.
 */
@ToString
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payments {

    /**
     * Вид оплаты. Возможные значения:
     * - «1» – электронный;
     * - «2» – «9» – расширенные типы оплаты.
     * Для каждого фискального типа оплаты можно указать расширенный тип оплаты.
     */
    private Integer type;

    /**
     * Сумма к оплате в рублях:
     * - целая часть не более 8 знаков;
     * - дробная часть не более 2 знаков
     */
    private BigDecimal sum;

}
