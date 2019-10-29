package com.rbkmoney.adapter.atol.converter;


import com.rbkmoney.adapter.atol.utils.extractors.TypeExtractor;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.provider.CashRegProviderSrv;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import com.rbkmoney.woody.api.flow.error.WErrorDefinition;
import com.rbkmoney.woody.api.flow.error.WErrorType;
import com.rbkmoney.woody.api.flow.error.WRuntimeException;
import com.rbkmoney.woody.api.trace.context.TraceContext;
import com.rbkmoney.woody.thrift.impl.http.error.THTransportErrorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

@Slf4j
@RequiredArgsConstructor
public class CashRegAdapterServiceLogDecorator implements CashRegProviderSrv.Iface {

    private final CashRegProviderSrv.Iface adapterService;

    @Override
    public CashRegResult register(CashRegContext cashRegContext) throws TException {
        String cashRegType = TypeExtractor.extractCashRegType(cashRegContext);
        String cashRegId = cashRegContext.getCashregId();
        log.info("Started: {} with cashRegId {}", cashRegType, cashRegId);
        try {
            CashRegResult regResult = adapterService.register(cashRegContext);
            log.info("Finished {} with cashRegId {}", cashRegType, cashRegId);
            return regResult;
        } catch (Exception ex) {
            String message = "Failed handle " + cashRegType + " with cashRegId " + cashRegId;
            logMessage(ex, message);
            throw ex;
        }
    }

    private static void logMessage(Exception ex, String message) {
        if (isUndefinedResultOrUnavailable(ex)) {
            log.warn(message, ex);
        } else {
            log.error(message, ex);
        }
    }

    private static boolean isUndefinedResultOrUnavailable(Exception exception) {
        WErrorDefinition definition;
        if (exception instanceof WRuntimeException) {
            definition = ((WRuntimeException) exception).getErrorDefinition();
        } else {
            THTransportErrorMapper errorMapper = new THTransportErrorMapper();
            definition = errorMapper.mapToDef(exception, TraceContext.getCurrentTraceData().getActiveSpan());
        }

        boolean undefined = definition != null && WErrorType.UNDEFINED_RESULT.getKey().equals(definition.getErrorType().getKey());
        boolean unavailable = definition != null && WErrorType.UNAVAILABLE_RESULT.getKey().equals(definition.getErrorType().getKey());
        return undefined || unavailable;
    }
}
