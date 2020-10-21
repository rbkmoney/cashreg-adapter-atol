package com.rbkmoney.adapter.atol.service.atol.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {

    @JsonProperty("email")
    private String email;

    @JsonProperty("sno")
    private String sno;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("payment_address")
    private String paymentAddress;

}
