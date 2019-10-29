package com.rbkmoney.adapter.atol.processor;

import com.rbkmoney.adapter.atol.model.EntryStateModel;
import com.rbkmoney.adapter.atol.model.ExitStateModel;

public interface Processor<T extends ExitStateModel, E extends EntryStateModel, R> {
    T process(R response, E context);
}


