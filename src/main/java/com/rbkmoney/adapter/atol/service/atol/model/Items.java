package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Items {

    /**
     * Наименование товара
     * Максимальная длина строки – 64 символа.
     */
    private String name;

    /**
     * Цена в рублях:
     * - целая часть не более 8 знаков;
     * - дробная часть не более 2 знаков
     */
    private BigDecimal price;

    /**
     * Количество/вес:
     * - целая часть не более 8 знаков;
     * - дробная часть не более 3 знаков.
     */
    private BigDecimal quantity;

    /**
     * Сумма позиции в рублях:
     * - целая часть не более 8 знаков;
     * - дробная часть не более 2 знаков.
     * Если значение sum меньше/больше значения (price*quantity), то разница является
     * скидкой/надбавкой на позицию соответственно. В этих случаях происходит
     * перерасчёт поля price для равномерного распределения скидки/надбавки по позициям.
     */
    private BigDecimal sum;

    /**
     * Единица измерения товара, работы, услуги, платежа, выплаты, иного предмета расчета.
     * Максимальная длина строки – 16 символов.
     */
    @JsonProperty("measurement_unit")
    private String measurementUnit;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_object")
    private String paymentObject;

    @JsonProperty("vat")
    private Vat vat;

}
