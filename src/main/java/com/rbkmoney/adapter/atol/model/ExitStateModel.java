package com.rbkmoney.adapter.atol.model;

import com.rbkmoney.damsel.cashreg.CashRegInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExitStateModel {
    private Step nextStep;
    private String errorCode;
    private String errorMessage;
    private AdapterContext adapterContext;
    private EntryStateModel entryStateModel;
    private String cashRegId;
    private CashRegInfo cashRegInfo;
}
