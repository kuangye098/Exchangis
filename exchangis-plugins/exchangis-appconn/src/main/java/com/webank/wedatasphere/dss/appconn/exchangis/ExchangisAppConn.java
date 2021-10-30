package com.webank.wedatasphere.dss.appconn.exchangis;

import com.webank.wedatasphere.dss.appconn.core.ext.ThirdlyAppConn;
import com.webank.wedatasphere.dss.appconn.core.impl.AbstractOnlySSOAppConn;
import com.webank.wedatasphere.dss.standard.app.development.standard.DevelopmentIntegrationStandard;
import com.webank.wedatasphere.dss.standard.app.structure.StructureIntegrationStandard;
import com.webank.wedatasphere.dss.appconn.exchangis.standard.ExchangisDevelopmentIntergrationStandard;
import com.webank.wedatasphere.dss.appconn.exchangis.standard.ExchangisStructureIntegrationStandard;

public class ExchangisAppConn extends AbstractOnlySSOAppConn implements ThirdlyAppConn {

    private DevelopmentIntegrationStandard developmentIntegrationStandard;
    private StructureIntegrationStandard structureIntegrationStandard;


    @Override
    protected void initialize() {
        developmentIntegrationStandard = new ExchangisDevelopmentIntergrationStandard();
        structureIntegrationStandard = new ExchangisStructureIntegrationStandard();
    }


    @Override
    public DevelopmentIntegrationStandard getOrCreateDevelopmentStandard() {
        return developmentIntegrationStandard;
    }

    @Override
    public StructureIntegrationStandard getOrCreateStructureStandard() {
        return structureIntegrationStandard;
    }
}