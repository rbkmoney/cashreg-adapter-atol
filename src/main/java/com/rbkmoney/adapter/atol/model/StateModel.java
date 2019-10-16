package com.rbkmoney.adapter.atol.model;

import com.rbkmoney.adapter.atol.constant.TargetType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StateModel {

    private TargetType targetType;
    private AdapterContext adapterContext;
    private Step step;
}
