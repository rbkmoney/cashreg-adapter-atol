package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payload {

    /**
     * Итоговая сумма документа в рублях с заданным в CMS
     * округлением:
     *  целая часть не более 8 знаков;
     *  дробная часть не более 2 знаков.
     * При регистрации в ККТ происходит расчёт фактической
     * суммы: суммирование значений sum позиций.
     */
    private Long total;

    /**
     * Адрес сайта ФНС
     */
    @JsonProperty("fns_site")
    private String fnsSite;

    /**
     * Номер ФН
     */
    @JsonProperty("fn_number")
    private String fnNumber;

    /**
     * Номер смены
     */
    @JsonProperty("shift_number")
    private Long shiftNumber;

    /**
     * Дата и время документа из ФН
     */
    @JsonProperty("receipt_datetime")
    private String receiptDatetime;

    /**
     * Номер чека всмене.
     */
    @JsonProperty("fiscal_receipt_number")
    private Long fiscalReceiptNumber;

    /**
     * Фискальный номер документа
     */
    @JsonProperty("fiscal_document_number")
    private Long fiscalDocumentNumber;

    /**
     * Регистрационный номер ККТ
     */
    @JsonProperty("ecr_registration_number")
    private String ecrRegistrationNumber;

    /**
     * Фискальный признак документа
     */
    @JsonProperty("fiscal_document_attribute")
    private Long fiscalDocumentAttribute;

}
