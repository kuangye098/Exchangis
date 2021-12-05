package com.webank.wedatasphere.dss.appconn.exchangis.project;

import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisConf;
import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisExceptionUtils;
import com.webank.wedatasphere.dss.appconn.exchangis.action.ExchangisDeleteAction;
import com.webank.wedatasphere.dss.appconn.exchangis.action.ExchangisPostAction;
import com.webank.wedatasphere.dss.standard.app.sso.builder.SSOUrlBuilderOperation;
import com.webank.wedatasphere.dss.standard.app.sso.request.SSORequestOperation;
import com.webank.wedatasphere.dss.standard.app.structure.StructureService;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectCreationOperation;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectDeletionOperation;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectRequestRef;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectResponseRef;
import com.webank.wedatasphere.dss.standard.common.entity.ref.ResponseRef;
import com.webank.wedatasphere.dss.standard.common.exception.operation.ExternalOperationFailedException;
import com.webank.wedatasphere.linkis.httpclient.request.HttpAction;
import com.webank.wedatasphere.linkis.httpclient.response.HttpResult;
import com.webank.wedatasphere.linkis.server.conf.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangisProjectDeleteOperation implements ProjectDeletionOperation, ExchangisConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisProjectDeleteOperation.class);

    private SSORequestOperation<HttpAction, HttpResult> ssoRequestOperation;

    private StructureService structureService;

    private final static String projectUrl = "/api/rest_s/" + ServerConfiguration.BDP_SERVER_VERSION() + "/exchangis/projects/{}";

    public ExchangisProjectDeleteOperation(StructureService structureService, SSORequestOperation<HttpAction, HttpResult> ssoRequestOperation) {
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
    public ResponseRef deleteProject(ProjectRequestRef requestRef) throws ExternalOperationFailedException {

        String actualProjectUrl = String.format(projectUrl,requestRef.getId());
        String url = getBaseUrl() + actualProjectUrl;

        LOGGER.info("project delete operation .....ProjectRequestRef = {},projectUrl = {} " +
                "            , dssUrl = {}.",requestRef,actualProjectUrl,url);

        ExchangisProjectResponseRef responseRef = null;

        ExchangisDeleteAction deleteAction = new ExchangisDeleteAction();
        deleteAction.setUrl(actualProjectUrl);
        deleteAction.setUser(requestRef.getCreateBy());
        deleteAction.setParameter("id", requestRef.getId());

        SSOUrlBuilderOperation ssoUrlBuilderOperation = requestRef.getWorkspace().getSSOUrlBuilderOperation().copy();
        ssoUrlBuilderOperation.setAppName(APP_NAME.getValue());
        ssoUrlBuilderOperation.setReqUrl(url);
        ssoUrlBuilderOperation.setWorkspace(requestRef.getWorkspace().getWorkspaceName());

        HttpResult httpResponse;
        try {
            httpResponse = this.ssoRequestOperation.requestWithSSO(ssoUrlBuilderOperation,deleteAction);
            LOGGER.info("{}, exchangis {}", requestRef.getName(), httpResponse.getResponseBody());
            if(httpResponse.getStatusCode() == 200){
                responseRef = new ExchangisProjectResponseRef(httpResponse.getResponseBody(),0);
                responseRef.setErrorMsg("delete project in exchangis successfully.");
            }else {
                ExchangisExceptionUtils.dealErrorException(60051,
                        "failed to delete project in exchangis : " + responseRef.getErrorMsg(),
                        ExternalOperationFailedException.class);
            }
        } catch (Throwable t) {
            ExchangisExceptionUtils.dealErrorException(60051, "failed to delete project in exchangis", t, ExternalOperationFailedException.class);
        }
        return responseRef;
    }
}
