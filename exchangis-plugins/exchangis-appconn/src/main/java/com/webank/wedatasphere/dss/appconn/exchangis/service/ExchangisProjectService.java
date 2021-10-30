package com.webank.wedatasphere.dss.appconn.exchangis.service;

import com.webank.wedatasphere.dss.appconn.exchangis.operation.ExchangisProjectCreationOperation;
import com.webank.wedatasphere.dss.standard.app.structure.StructureIntegrationStandard;
import com.webank.wedatasphere.dss.standard.app.structure.project.*;
import com.webank.wedatasphere.dss.standard.common.desc.AppInstance;
import com.webank.wedatasphere.dss.standard.common.service.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExchangisProjectService extends ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisProjectService.class);

    private AppInstance appInstance;

    private StructureIntegrationStandard structureIntegrationStandard;

    private Map<Class<? extends Operation>, Operation<?, ?>> operationMap = new ConcurrentHashMap<>();

    public Operation createOperation(Class<? extends Operation> clazz) {
        return null;
    }

    public boolean isOperationExists(Class<? extends Operation> clazz) {
        return false;
    }

    public boolean isOperationNecessary(Class<? extends Operation> clazz) {
        return false;
    }

    public void setAppStandard(StructureIntegrationStandard appStandard) {
        this.structureIntegrationStandard = appStandard;
    }

    public StructureIntegrationStandard getAppStandard() {
        return this.structureIntegrationStandard;
    }

    public AppInstance getAppInstance() {
        return this.appInstance;
    }

    public void setAppInstance(AppInstance appInstance) {
        this.appInstance = appInstance;
    }

    public ProjectCreationOperation createProjectCreationOperation() {
        if (this.operationMap.containsKey(ExchangisProjectCreationOperation.class))
            return (ProjectCreationOperation)this.operationMap.get(ExchangisProjectCreationOperation.class);
        this.operationMap.put(ExchangisProjectCreationOperation.class, new ExchangisProjectCreationOperation(this));
        return (ProjectCreationOperation)this.operationMap.get(ExchangisProjectCreationOperation.class);
    }

    public ProjectUpdateOperation createProjectUpdateOperation() {
        return null;
    }

    public ProjectDeletionOperation createProjectDeletionOperation() {
        return null;
    }

    public ProjectUrlOperation createProjectUrlOperation() {
        return null;
    }
}
