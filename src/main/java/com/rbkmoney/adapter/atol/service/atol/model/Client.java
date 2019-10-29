package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.adapter.atol.service.atol.constant.Sno;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client {

    /**
     * Электронная почта покупателя
     * Максимальная длина строки – 64 символа.
     * <p>
     * e.g. sample@gmail.ru
     */
    private String email;

    /**
     * Телефон покупателя.
     * Номер телефона необходимо передать вместе с
     * кодом страны без пробелов и дополнительных
     * символов, кроме символа «+» (номер «+371 2
     * 1234567» необходимо передать как
     * «+37121234567»).
     * Если номер телефона относится к России
     * (префикс «+7»), то значение можно передать
     * без префикса (номер «+7 925 1234567» можно
     * передать как «9251234567»).
     * Максимальная длина строки – 64 символа.
     * В запросе обязательно должно быть заполнено
     * хотя бы одно из полей: email или phone.
     */
    private String phone;

    /**
     * Система налогообложения.
     * <p>
     * e.g. osn {@link Sno}
     */
    private String sno;

}
