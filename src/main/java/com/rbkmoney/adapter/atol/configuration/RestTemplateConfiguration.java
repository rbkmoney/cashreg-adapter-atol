package com.rbkmoney.adapter.atol.configuration;

import com.rbkmoney.woody.api.trace.ContextUtils;
import com.rbkmoney.woody.api.trace.context.TraceContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;


@Configuration
public class RestTemplateConfiguration {

    @Bean
    public SSLContext sslContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return new SSLContextBuilder()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(SSLContext sslContext) {
        return HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(sslContext)
                .disableAutomaticRetries()
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory requestFactory(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(
            HttpComponentsClientHttpRequestFactory requestFactory,
            MetricsRestTemplateCustomizer metricsRestTemplateCustomizer
    ) {
        return new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
                .additionalCustomizers(metricsRestTemplateCustomizer);
    }

    @Bean
    // TODO: 14/11/2018 fix integrations test: change {@Autowired ServerHandler serverHandler} to thriftClient
    @RequestScope
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                                     @Value("${restTemplate.networkTimeout}") int networkTimeout) {
        int executionTimeout = ContextUtils.getExecutionTimeout(TraceContext.getCurrentTraceData().getServiceSpan(), networkTimeout);
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(executionTimeout))
                .setReadTimeout(Duration.ofMillis(executionTimeout))
                .build();
        restTemplate.setErrorHandler(getDefaultResponseErrorHandler());
        setMessageConverter(restTemplate);
        return restTemplate;
    }

    private void setMessageConverter(RestTemplate restTemplate) {
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
    }

    private DefaultResponseErrorHandler getDefaultResponseErrorHandler() {
        return new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        };
    }

}
