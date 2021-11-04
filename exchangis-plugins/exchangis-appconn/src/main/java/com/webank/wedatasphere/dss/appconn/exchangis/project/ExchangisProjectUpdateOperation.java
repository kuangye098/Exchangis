package com.webank.wedatasphere.dss.appconn.exchangis.project;

import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisConf;
import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisExceptionUtils;
import com.webank.wedatasphere.dss.appconn.exchangis.action.ExchangisPutAction;
import com.webank.wedatasphere.dss.standard.app.sso.builder.SSOUrlBuilderOperation;
import com.webank.wedatasphere.dss.standard.app.sso.request.SSORequestOperation;
import com.webank.wedatasphere.dss.standard.app.structure.StructureService;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectRequestRef;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectResponseRef;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectUpdateOperation;
import com.webank.wedatasphere.dss.standard.common.exception.operation.ExternalOperationFailedException;
import com.webank.wedatasphere.linkis.httpclient.request.HttpAction;
import com.webank.wedatasphere.linkis.httpclient.response.HttpResult;
import com.webank.wedatasphere.linkis.server.conf.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangisProjectUpdateOperation implements ProjectUpdateOperation, ExchangisConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisProjectUpdateOperation.class);

    private SSORequestOperation<HttpAction, HttpResult> ssoRequestOperation;

    private StructureService structureService;

    private final static String projectUrl = "/api/rest_s/" + ServerConfiguration.BDP_SERVER_VERSION() + "/exchangis/updateProject";

    public ExchangisProjectUpdateOperation(StructureService structureService, SSORequestOperation<HttpAction, HttpResult> ssoRequestOperation) {
        this.structureService = structureService;
        this.ssoRequestOperation = ssoRequestOperation;
    }

    public void init() {

    }

    public void setStructureService(StructureService structureService) {
        this.structureService = structureService;
    }

    private String getBaseUrl(){
        return structureService.getAppInstance().getBaseUrl();
    }

    @Override
    public ProjectResponseRef updateProject(ProjectRequestRef projectRequestRef) throws ExternalOperationFailedException {

        String url = getBaseUrl() + projectUrl;

        LOGGER.info("project update operation .....ProjectRequestRef = {},projectUrl = {} " +
                "            , dssUrl = {}.",projectRequestRef,this.projectUrl,url);

        ExchangisProjectResponseRef responseRef = null;

        ExchangisPutAction putAction = new ExchangisPutAction();
        putAction.setUrl(projectUrl);
        putAction.setUser(projectRequestRef.getCreateBy());
        putAction.addRequestPayload("workspaceName", projectRequestRef.getWorkspaceName());
        putAction.addRequestPayload("projectName", projectRequestRef.getName());
        putAction.addRequestPayload("createdBy", projectRequestRef.getCreateBy());
        putAction.addRequestPayload("description", projectRequestRef.getDescription());
        putAction.addRequestPayload("id", projectRequestRef.getId());

        SSOUrlBuilderOperation ssoUrlBuilderOperation = projectRequestRef.getWorkspace().getSSOUrlBuilderOperation().copy();
        ssoUrlBuilderOperation.setAppName(APP_NAME.getValue());
        ssoUrlBuilderOperation.setReqUrl(url);
        ssoUrlBuilderOperation.setWorkspace(projectRequestRef.getWorkspace().getWorkspaceName());

        HttpResult httpResponse;
        try {
            httpResponse = this.ssoRequestOperation.requestWithSSO(ssoUrlBuilderOperation,putAction);
            LOGGER.info("{}, exchangis {}", projectRequestRef.getName(), httpResponse.getResponseBody());
            if(httpResponse.getStatusCode() == 200){
                responseRef = new ExchangisProjectResponseRef(httpResponse.getResponseBody(),0);
                responseRef.setProjectRefId((Long)responseRef.toMap().get("id"));
            }else {
                ExchangisExceptionUtils.dealErrorException(60051,
                        "failed to update project in exchangis : " + responseRef.getErrorMsg(),
                        ExternalOperationFailedException.class);
            }
        } catch (Throwable t) {
            ExchangisExceptionUtils.dealErrorException(60051, "failed to update project in exchangis", t, ExternalOperationFailedException.class);
        }
        return responseRef;
    }
}
