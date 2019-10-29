package com.rbkmoney.adapter.atol.flow;

import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.StateModel;
import com.rbkmoney.adapter.atol.model.Step;

public interface StepResolver<T extends StateModel, R extends ExitStateModel>  {

    Step resolveEntry(T stateModel);

    Step resolveExit(R stateModel);

}
