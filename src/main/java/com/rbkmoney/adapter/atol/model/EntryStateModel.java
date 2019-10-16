package com.rbkmoney.adapter.atol.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntryStateModel {
    private OperationModel operationModel;
    private StateModel stateModel;
}
