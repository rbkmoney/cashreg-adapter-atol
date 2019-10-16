package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Служебный раздел
 */
@ToString
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service {

    /**
     * URL, на который необходимо ответить после обработки документа.
     * Если поле заполнено, то после обработки документа
     * (успешной или не успешной фискализации в ККТ),
     * ответ будет отправлен POST запросом по URL указанному в данном поле.
     * Максимальная длина строки – 256 символов.
     */
    @JsonProperty("callback_url")
    private String callbackUrl;

}
