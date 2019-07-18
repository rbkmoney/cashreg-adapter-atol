package com.rbkmoney.adapter.atol.service.atol.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTokenResponse {

    @JsonProperty("error")
    private com.rbkmoney.adapter.atol.service.atol.model.Error error;

    @JsonProperty("token")
    private String token;

    @JsonProperty("timestamp")
    private String timestamp;
}
