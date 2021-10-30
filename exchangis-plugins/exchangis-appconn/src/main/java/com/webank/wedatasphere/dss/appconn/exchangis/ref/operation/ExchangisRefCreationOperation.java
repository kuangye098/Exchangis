package com.webank.wedatasphere.dss.appconn.exchangis.ref.operation;


import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisConf;
import com.webank.wedatasphere.dss.standard.app.development.operation.RefCreationOperation;
import com.webank.wedatasphere.dss.standard.app.development.ref.CreateRequestRef;
import com.webank.wedatasphere.dss.standard.app.development.service.DevelopmentService;
import com.webank.wedatasphere.dss.standard.app.sso.builder.SSOUrlBuilderOperation;
import com.webank.wedatasphere.dss.standard.app.sso.origin.request.OriginSSORequestOperation;
import com.webank.wedatasphere.dss.standard.app.sso.request.SSORequestOperation;
import com.webank.wedatasphere.dss.standard.common.entity.ref.ResponseRef;
import com.webank.wedatasphere.dss.standard.common.exception.operation.ExternalOperationFailedException;
import com.webank.wedatasphere.dss.appconn.exchangis.action.ExchangisPostAction;
import com.webank.wedatasphere.dss.appconn.exchangis.ref.entity.ExchangisCreateRequestRef;
import com.webank.wedatasphere.dss.appconn.exchangis.ref.entity.ExchangisCreateResponseRef;
import com.webank.wedatasphere.linkis.httpclient.request.HttpAction;
import com.webank.wedatasphere.linkis.httpclient.response.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangisRefCreationOperation implements RefCreationOperation<CreateRequestRef> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisRefCreationOperation.class);

    private DevelopmentService developmentService;
    private SSORequestOperation<HttpAction, HttpResult> ssoRequestOperation;

    public ExchangisRefCreationOperation(DevelopmentService developmentService) {
        this.developmentService = developmentService;
        this.ssoRequestOperation = new OriginSSORequestOperation(ExchangisConf.APP_NAME.getValue());
    }

    @Override
    public ResponseRef createRef(CreateRequestRef requestRef) throws ExternalOperationFailedException {
        ExchangisCreateRequestRef createRequestRef = (ExchangisCreateRequestRef) requestRef;
        createRequestRef.setParameter("example-param1", "nutsjian");
        createRequestRef.setParameter("example-param2", "32");
        createRequestRef.setParameter("example-param3", "male");
        return requestCall(createRequestRef);
    }

    // 同步请求三方系统
    private ResponseRef requestCall(ExchangisCreateRequestRef requestRef) throws ExternalOperationFailedException {

        ExchangisPostAction exchangisPostAction = new ExchangisPostAction();
        exchangisPostAction.addRequestPayload("name", requestRef.getName());
        exchangisPostAction.addRequestPayload("projectId", requestRef.getParameter("projectId"));

        SSOUrlBuilderOperation ssoUrlBuilderOperation = requestRef.getWorkspace().getSSOUrlBuilderOperation().copy();
        ssoUrlBuilderOperation.setAppName(ExchangisConf.APP_NAME.getValue());
        ssoUrlBuilderOperation.setReqUrl(getBaseUrl() + "/development/create");
        ssoUrlBuilderOperation.setWorkspace(requestRef.getWorkspace().getWorkspaceName());

        ResponseRef responseRef;
        try{
            LOGGER.info("exchangisPostAction  =>  {},ssoUrlBuilderOperation builtUrl => {}",
                    exchangisPostAction,ssoUrlBuilderOperation.getBuiltUrl());
            exchangisPostAction.setUrl(ssoUrlBuilderOperation.getBuiltUrl());
            //
            HttpResult httpResult = this.ssoRequestOperation.requestWithSSO(ssoUrlBuilderOperation, exchangisPostAction);
            responseRef = new ExchangisCreateResponseRef(httpResult.getResponseBody());
        } catch (Exception e){
            throw new ExternalOperationFailedException(90177, "Create Widget Exception", e);
        }
        // cs
//        VisualisRefUpdateOperation visualisRefUpdateOperation = new VisualisRefUpdateOperation(developmentService);
//        VisualisUpdateCSRequestRef visualisUpdateCSRequestRef = new VisualisUpdateCSRequestRef();
//        visualisUpdateCSRequestRef.setContextID((String) requestRef.getJobContent().get(CSCommonUtils.CONTEXT_ID_STR));
//        visualisUpdateCSRequestRef.setJobContent(responseRef.toMap());
//        visualisUpdateCSRequestRef.setUserName(requestRef.getUsername());
//        visualisUpdateCSRequestRef.setNodeType(requestRef.getNodeType());
//        visualisUpdateCSRequestRef.setWorkspace(requestRef.getWorkspace());
//        visualisRefUpdateOperation.updateRef(visualisUpdateCSRequestRef);
        return responseRef;
    }

    private String getBaseUrl() {
        return this.developmentService.getAppInstance().getBaseUrl();
    }

    @Override
    public void setDevelopmentService(DevelopmentService developmentService) {
        this.developmentService = developmentService;
    }
}
