package com.webank.wedatasphere.dss.appconn.exchangis.operation;

import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisConf;
import com.webank.wedatasphere.dss.appconn.exchangis.ExchangisExceptionUtils;
import com.webank.wedatasphere.dss.standard.app.sso.builder.impl.SSOUrlBuilderOperationImpl;
import com.webank.wedatasphere.dss.standard.app.sso.request.SSORequestOperation;
import com.webank.wedatasphere.dss.standard.app.structure.StructureService;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectCreationOperation;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectRequestRef;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectResponseRef;
import com.webank.wedatasphere.dss.standard.common.exception.operation.ExternalOperationFailedException;
import com.webank.wedatasphere.dss.appconn.exchangis.ref.ExchangisProjectResponseRef;
import com.webank.wedatasphere.dss.appconn.exchangis.service.ExchangisProjectService;
import com.webank.wedatasphere.dss.appconn.exchangis.sso.ExchangisHttpPost;
import com.webank.wedatasphere.dss.appconn.exchangis.sso.ExchangisPostRequestOperation;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExchangisProjectCreationOperation implements ProjectCreationOperation, ExchangisConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangisProjectCreationOperation.class);

    private ExchangisProjectService exchangisProjectService;

    private SSOUrlBuilderOperationImpl ssoUrlBuilderOperation;

    private SSORequestOperation<ExchangisHttpPost, CloseableHttpResponse> postOperation;

    private String projectUrl;

    public ExchangisProjectCreationOperation(ExchangisProjectService exchangisProjectService) {
        this.exchangisProjectService = exchangisProjectService;
        init();
    }

    public void init() {
        this.ssoUrlBuilderOperation = new SSOUrlBuilderOperationImpl();
        this.ssoUrlBuilderOperation.setAppName(ExchangisConf.APP_NAME.getValue());
        this.postOperation = new ExchangisPostRequestOperation(this.exchangisProjectService.getAppInstance().getBaseUrl());
        this.projectUrl = this.exchangisProjectService.getAppInstance().getBaseUrl().endsWith("/") ? (this.exchangisProjectService.getAppInstance().getBaseUrl() + "exchangis/createProject") : (this.exchangisProjectService.getAppInstance().getBaseUrl() + "/exchangis/createProject");
    }

    public void setStructureService(StructureService service) {
        if (service instanceof ExchangisProjectService)
            this.exchangisProjectService = (ExchangisProjectService)service;
    }

    public ProjectResponseRef createProject(ProjectRequestRef requestRef) throws ExternalOperationFailedException {

        LOGGER.info("example project create operation .....ProjectRequestRef = {},projectUrl = {} " +
                "            , dssUrl = {}.",requestRef,this.projectUrl,this.ssoUrlBuilderOperation.getDssUrl());

        ExchangisProjectResponseRef responseRef = new ExchangisProjectResponseRef();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("workspaceName", requestRef.getWorkspaceName()));
        params.add(new BasicNameValuePair("name", requestRef.getName()));
        params.add(new BasicNameValuePair("createdBy", requestRef.getCreateBy()));
        HttpEntity entity = EntityBuilder.create().setContentType(ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)).setParameters(params).build();
        ExchangisHttpPost httpPost = new ExchangisHttpPost(this.projectUrl, requestRef.getCreateBy());
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = this.postOperation.requestWithSSO(this.ssoUrlBuilderOperation, httpPost);
            HttpEntity ent = httpResponse.getEntity();
            String entStr = IOUtils.toString(ent.getContent(), StandardCharsets.UTF_8);
            LOGGER.error("{}, exchangis {}", requestRef.getName(), entStr);
            responseRef.setProjectRefId(0L);
        } catch (Throwable t) {
            ExchangisExceptionUtils.dealErrorException(60051, "failed to create project in example", t, ExternalOperationFailedException.class);
        } finally {
            IOUtils.closeQuietly(httpResponse);
        }
        return responseRef;
    }
}
