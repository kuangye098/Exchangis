package com.webank.wedatasphere.dss.appconn.exchangis.project;

import com.google.common.collect.Maps;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectResponseRef;
import com.webank.wedatasphere.dss.standard.common.desc.AppInstance;
import com.webank.wedatasphere.dss.standard.common.entity.ref.AbstractResponseRef;
import com.webank.wedatasphere.linkis.server.BDPJettyServerHelper;

import java.util.Map;

public class ExchangisProjectResponseRef extends AbstractResponseRef implements ProjectResponseRef {

    private String errorMsg;
    private Long projectRefId;
    private AppInstance appInstance;

    public ExchangisProjectResponseRef(String responseBody,int status) throws Exception {
        super(responseBody, status);
        responseMap = BDPJettyServerHelper.jacksonJson().readValue(responseBody, Map.class);
    }

    public Map<String, Object> toMap() {
        return responseMap;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getProjectRefId() {
        return this.projectRefId;
    }

    public Map<AppInstance, Long> getProjectRefIds() {
        Map<AppInstance, Long> projectRefIdsMap = Maps.newHashMap();
        projectRefIdsMap.put(appInstance, projectRefId);
        return projectRefIdsMap;
    }

    public void setProjectRefId(Long projectRefId) {
        this.projectRefId = projectRefId;
    }

    public AppInstance getAppInstance() {
        return appInstance;
    }

    public void setAppInstance(AppInstance appInstance) {
        this.appInstance = appInstance;
    }
}
