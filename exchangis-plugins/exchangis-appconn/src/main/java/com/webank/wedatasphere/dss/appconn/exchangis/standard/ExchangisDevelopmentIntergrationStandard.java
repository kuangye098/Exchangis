package com.webank.wedatasphere.dss.appconn.exchangis.standard;

import com.webank.wedatasphere.dss.appconn.exchangis.ref.service.ExchangisCRUDService;
import com.webank.wedatasphere.dss.standard.app.development.service.*;
import com.webank.wedatasphere.dss.standard.app.development.standard.AbstractDevelopmentIntegrationStandard;
import com.webank.wedatasphere.dss.standard.common.exception.AppStandardErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExchangisDevelopmentIntergrationStandard extends AbstractDevelopmentIntegrationStandard {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisDevelopmentIntergrationStandard.class);

    @Override
    protected RefCRUDService createRefCRUDService() {
        return new ExchangisCRUDService();
    }

    @Override
    protected RefExecutionService createRefExecutionService() {
        return null;
    }

    @Override
    protected RefExportService createRefExportService() {
        return null;
    }

    @Override
    protected RefImportService createRefImportService() {
        return null;
    }

    @Override
    protected RefQueryService createRefQueryService() {
        return null;
    }

    @Override
    public void init() throws AppStandardErrorException {

    }

    @Override
    public void close() throws IOException {

    }
}
