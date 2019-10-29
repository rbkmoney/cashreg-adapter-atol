package com.rbkmoney.adapter.atol.handler.cashreg;

import com.rbkmoney.adapter.atol.model.EntryStateModel;
import org.apache.thrift.TException;

public interface CommonHandler<T, R, E extends EntryStateModel> {

    boolean isHandler(final E entryStateModel);

    T handle(E context) throws TException;

}
