package com.rbkmoney.adapter.atol.service.atol.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestWrapper<R> {
    private R request;
    private String url;
    private String group;
    private String company;
    private String companyAddress;
    private String token;
}
