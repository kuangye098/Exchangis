package com.webank.wedatasphere.dss.appconn.exchangis.standard;

import com.webank.wedatasphere.dss.standard.app.sso.request.SSORequestService;
import com.webank.wedatasphere.dss.standard.app.structure.StructureIntegrationStandard;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectService;
import com.webank.wedatasphere.dss.standard.app.structure.role.RoleService;
import com.webank.wedatasphere.dss.standard.app.structure.status.AppStatusService;
import com.webank.wedatasphere.dss.standard.common.desc.AppInstance;
import com.webank.wedatasphere.dss.appconn.exchangis.project.ExchangisProjectService;

public class ExchangisStructureIntegrationStandard implements StructureIntegrationStandard {

    @Override
    public void init() {

    }

    @Override
    public RoleService getRoleService(AppInstance appInstance) {
        return null;
    }

    @Override
    public ProjectService getProjectService(AppInstance appInstance) {
        return new ExchangisProjectService();
    }

    @Override
    public AppStatusService getAppStateService(AppInstance appInstance) {
        return null;
    }

    @Override
    public void setSSORequestService(SSORequestService ssoRequestService) {

    }

    @Override
    public void close() {}
}
