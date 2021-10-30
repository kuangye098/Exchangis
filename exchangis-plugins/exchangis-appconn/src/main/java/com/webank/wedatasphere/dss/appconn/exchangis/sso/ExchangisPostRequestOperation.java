package com.webank.wedatasphere.dss.appconn.exchangis.sso;

import com.webank.wedatasphere.dss.standard.app.sso.builder.SSOUrlBuilderOperation;
import com.webank.wedatasphere.dss.standard.app.sso.request.SSORequestOperation;
import com.webank.wedatasphere.dss.standard.common.exception.AppStandardErrorException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangisPostRequestOperation implements SSORequestOperation<ExchangisHttpPost, CloseableHttpResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisPostRequestOperation.class);

    private ExchangisSecurityService exchangisSecurityService;

    private CloseableHttpClient httpClient;

    public ExchangisPostRequestOperation(String baseUrl) {
        this.exchangisSecurityService = ExchangisSecurityService.getInstance(baseUrl);
    }

    public CloseableHttpResponse requestWithSSO(SSOUrlBuilderOperation urlBuilder, ExchangisHttpPost req) throws AppStandardErrorException {
        try {
            LOGGER.info("");
            this.httpClient = HttpClients.custom().build();
            return this.httpClient.execute(req);
        } catch (Throwable t) {
            throw new AppStandardErrorException(90009, t.getMessage(), t);
        } finally {}
    }

    public CloseableHttpResponse requestWithSSO(String url, ExchangisHttpPost req) throws AppStandardErrorException {
        return null;
    }
}
