package com.rbkmoney.adapter.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdapterContext {

    @JsonProperty(value = "max_date_time_polling")
    private Instant maxDateTimePolling;

    private Step nextStep;

    @JsonProperty(value = "cashreg_id")
    private String cashRegId;

    @JsonProperty(value = "receipt_id")
    private String receiptId;

}
