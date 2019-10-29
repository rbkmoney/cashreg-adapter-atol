package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Чек
 */
@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Receipt {

    @JsonProperty("client")
    private Client client;

    @JsonProperty("company")
    private Company company;

    /**
     * Ограничение по количеству от 1 до 100.
     */
    @JsonProperty("items")
    private List<Items> items;

    /**
     * Оплата
     * Ограничение по количеству от 1 до 10.
     */
    @JsonProperty("payments")
    private List<Payments> payments;

    @JsonProperty("vats")
    private List<Vat> vats;

    /**
     * Итоговая сумма чека в рублях с заданным в
     * CMS округлением:
     * - целая часть не более 8 знаков;
     * - дробная часть не более 2 знаков
     * <p>
     * При регистрации в ККТ происходит расчёт
     * фактической суммы: суммирование значений
     * sum позиций
     */
    private BigDecimal total;
}
