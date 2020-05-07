package com.rbkmoney.adapter.atol.servlet;

import com.rbkmoney.damsel.cashreg.adapter.CashregAdapterSrv;
import com.rbkmoney.woody.thrift.impl.http.THServiceBuilder;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@RequiredArgsConstructor
@WebServlet("/adapter/cashreg/atol")
public class AdapterServlet extends GenericServlet {

    private final CashregAdapterSrv.Iface handler;

    private Servlet servlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servlet = new THServiceBuilder().build(CashregAdapterSrv.Iface.class, handler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        servlet.service(request, response);
    }

}

