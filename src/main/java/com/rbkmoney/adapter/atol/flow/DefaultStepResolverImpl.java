package com.rbkmoney.adapter.atol.flow;

import com.rbkmoney.adapter.atol.model.ExitStateModel;
import com.rbkmoney.adapter.atol.model.StateModel;
import com.rbkmoney.adapter.atol.model.Step;
import org.springframework.stereotype.Component;

@Component
public class DefaultStepResolverImpl implements StepResolver<StateModel, ExitStateModel> {

    @Override
    public Step resolveEntry(StateModel stateModel) {
        if(stateModel.getStep() == null) {
            return Step.CREATE;
        }
        return Step.CHECK_STATUS;
    }

    @Override
    public Step resolveExit(ExitStateModel stateModel) {
        return stateModel.getNextStep();
    }
}
