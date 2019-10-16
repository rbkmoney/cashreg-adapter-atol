package com.rbkmoney.adapter.atol.model;


import com.rbkmoney.adapter.atol.service.atol.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class OperationModel {

    private String cashRegId;

    private Auth auth;
    private Company company;
    private Client client;

    private List<Items> items;
    private List<Payments> payments;
    private List<Vat> vats;

    private BigDecimal total;

    @ToString.Exclude
    private Map<String, String> options;

    private String callbackUrl;

}
