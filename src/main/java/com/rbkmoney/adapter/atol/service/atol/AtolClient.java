package com.rbkmoney.adapter.atol.service.atol;

import com.rbkmoney.adapter.atol.configuration.properties.AtolProperties;
import com.rbkmoney.adapter.atol.service.atol.constant.Sno;
import com.rbkmoney.adapter.atol.service.atol.model.*;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.response.GetTokenResponse;
import com.rbkmoney.adapter.atol.utils.DateFormate;
import com.rbkmoney.adapter.atol.utils.OptionsFields;
import com.rbkmoney.cashreg.proto.provider.CashRegContext;
import com.rbkmoney.cashreg.proto.provider.CompanyInfo;
import com.rbkmoney.cashreg.proto.provider.ItemsLine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.boot.autoconfigure.jms.JmsProperties.AcknowledgeMode.CLIENT;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class AtolClient implements AdapterCashReg {

    private final AtolApi api;
    private final AtolProperties atolProperties;

    @Override
    public ResponseEntity<?> debit(CashRegContext context) {

        Map<String, String> options = context.getOptions();
        String url = options.getOrDefault(OptionsFields.URL, atolProperties.getUrl());

        api.setUrl(url);

        CompanyInfo companyInfo = context.getAccountInfo().getCompanyInfo();
        api.setCompany(companyInfo.getName());
        api.setLogin(options.get(OptionsFields.LOGIN));
        api.setAddress(companyInfo.getAddress());
        api.setPassword(options.get(OptionsFields.PASS));

        if (options.get(OptionsFields.GROUP) != null) {
            api.setGroup(options.get(OptionsFields.GROUP));
        }

        ResponseEntity<GetTokenResponse> tokenResponse = api.getToken();
        api.setApiToken(tokenResponse.getBody().getToken());

        CommonRequest request = new CommonRequest();
        request.setExternalId(context.getRequestId());


        // Items - begin
        List<Items> itemsList = prepareCart(context.getPaymentInfo().getCart().getLines(), options);
        // Items - end

        if (itemsList.isEmpty()) {
            String message = String.format("%s List items is empty", CLIENT);
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        Receipt receipt = new Receipt();
        receipt.setItems(itemsList);

        Client client = new Client();
        client.setEmail(context.getPaymentInfo().getPayer().getContactInfo().getEmail());
        receipt.setClient(client);

        Company company = new Company();
        company.setSno(Sno.fromStringValue(companyInfo.getTaxMode().name()).getCode());
        company.setInn(companyInfo.getInn());
        company.setPaymentAddress(companyInfo.getAddress());
        company.setEmail(companyInfo.getEmail());
        receipt.setCompany(company);


        // Payments and vats
        List<Payments> paymentsList = new ArrayList<>();


        BigDecimal totalAmount = new BigDecimal(context.getPaymentInfo().getCash().getAmount())
                .divide(new BigDecimal(100)).setScale(2);

        Payments payments = new Payments();
        payments.setSum(totalAmount);
        payments.setType(1);
        paymentsList.add(payments);
        receipt.setPayments(paymentsList);

        receipt.setTotal(totalAmount);
        request.setReceipt(receipt);


        request.setTimestamp(DateFormate.getCurrentDate());

        return api.sell(request);
    }

    @Override
    public ResponseEntity<?> credit(CashRegContext context) {
        throw new RuntimeException("Method Credit Not Supported");
    }

    @Override
    public ResponseEntity<?> refundDebit(CashRegContext context) {


        Map<String, String> options = context.getOptions();
        String url = options.get(OptionsFields.URL);

        api.setUrl(url);

        CompanyInfo companyInfo = context.getAccountInfo().getCompanyInfo();
        api.setCompany(companyInfo.getName());
        api.setLogin(options.get(OptionsFields.LOGIN));
        api.setAddress(companyInfo.getAddress());
        api.setPassword(options.get(OptionsFields.PASS));

        if (options.get(OptionsFields.GROUP) != null) {
            api.setGroup(options.get(OptionsFields.GROUP));
        }

        ResponseEntity<GetTokenResponse> tokenResponse = api.getToken();
        api.setApiToken(tokenResponse.getBody().getToken());

        CommonRequest request = new CommonRequest();
        request.setExternalId(context.getRequestId());


        // Items - begin
        List<Items> itemsList = prepareCart(context.getPaymentInfo().getCart().getLines(), options);
        // Items - end

        if (itemsList.isEmpty()) {
            String message = String.format("%s List items is empty", CLIENT);
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        Receipt receipt = new Receipt();
        receipt.setItems(itemsList);

        Client client = new Client();
        client.setEmail(context.getPaymentInfo().getPayer().getContactInfo().getEmail());
        receipt.setClient(client);

        Company company = new Company();
        company.setSno(Sno.fromStringValue(companyInfo.getTaxMode().name()).getCode());
        company.setInn(companyInfo.getInn());
        company.setPaymentAddress(companyInfo.getAddress());
        company.setEmail(companyInfo.getEmail());
        receipt.setCompany(company);

        List<Payments> paymentsList = new ArrayList<>();

        BigDecimal totalAmount = new BigDecimal(context.getPaymentInfo().getCash().getAmount())
                .divide(new BigDecimal(100)).setScale(2);

        Payments payments = new Payments();
        payments.setSum(totalAmount);
        payments.setType(1);
        paymentsList.add(payments);
        receipt.setPayments(paymentsList);

        receipt.setTotal(totalAmount);
        request.setReceipt(receipt);


        request.setTimestamp(DateFormate.getCurrentDate());

        return api.sellRefund(request);
    }

    @Override
    public ResponseEntity<?> refundCredit(CashRegContext context) {
        throw new RuntimeException("Method Credit Not Supported");
    }

    @Override
    public ResponseEntity<?> getStatus(CashRegContext context) {
        Map<String, String> options = context.getOptions();
        String url = options.get(OptionsFields.URL);

        api.setUrl(url);

        CompanyInfo companyInfo = context.getAccountInfo().getCompanyInfo();
        api.setCompany(companyInfo.getName());
        api.setLogin(options.get(OptionsFields.LOGIN));
        api.setAddress(companyInfo.getAddress());
        api.setPassword(options.get(OptionsFields.PASS));

        if (options.get(OptionsFields.GROUP) != null) {
            api.setGroup(options.get(OptionsFields.GROUP));
        }

        ResponseEntity<GetTokenResponse> tokenResponse = api.getToken();
        api.setApiToken(tokenResponse.getBody().getToken());

        return api.report(context.getCashregInfo().getUuid());
    }

    public List<Items> prepareCart(List<ItemsLine> itemsLines, Map<String, String> options) {

        List<Items> itemsList = new ArrayList<>();
        if (!itemsLines.isEmpty()) {
            // Cart
            itemsLines.forEach(itemLine -> {
                Items item = new Items();
                item.setQuantity(new BigDecimal(itemLine.getQuantity()).setScale(1));
                item.setPrice(new BigDecimal(itemLine.getPrice().getAmount()).divide(new BigDecimal(100)).setScale(2));

                BigDecimal sum = new BigDecimal(itemLine.getPrice().getAmount())
                        .multiply(new BigDecimal(itemLine.getQuantity()))
                        .divide(new BigDecimal(100)).setScale(2);

                item.setSum(sum);

                String invoiceTaxId = itemLine.getTax();
                String taxId = (invoiceTaxId != null && !invoiceTaxId.isEmpty())
                        ? com.rbkmoney.adapter.atol.service.atol.constant.Vat.codeTextOf(invoiceTaxId).getCode()
                        : com.rbkmoney.adapter.atol.service.atol.constant.Vat.NO_VAT.getCode();


                Vat vat = new Vat();
                vat.setType(taxId);
                item.setVat(vat);

                item.setPaymentMethod(options.get(OptionsFields.PAYMENT_METHOD));
                item.setPaymentObject(options.get(OptionsFields.PAYMENT_OBJECT));

                item.setName(itemLine.getProduct());
                itemsList.add(item);
            });

        }

        return itemsList;
    }
}
