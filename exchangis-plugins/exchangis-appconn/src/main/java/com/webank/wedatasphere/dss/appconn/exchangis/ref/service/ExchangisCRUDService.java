package com.webank.wedatasphere.dss.appconn.exchangis.ref.service;


import com.webank.wedatasphere.dss.appconn.exchangis.ref.operation.ExchangisRefCreationOperation;
import com.webank.wedatasphere.dss.standard.app.development.operation.RefCopyOperation;
import com.webank.wedatasphere.dss.standard.app.development.operation.RefCreationOperation;
import com.webank.wedatasphere.dss.standard.app.development.operation.RefDeletionOperation;
import com.webank.wedatasphere.dss.standard.app.development.operation.RefUpdateOperation;
import com.webank.wedatasphere.dss.standard.app.development.ref.CopyRequestRef;
import com.webank.wedatasphere.dss.standard.app.development.ref.CreateRequestRef;
import com.webank.wedatasphere.dss.standard.app.development.ref.DeleteRequestRef;
import com.webank.wedatasphere.dss.standard.app.development.service.AbstractRefCRUDService;
import com.webank.wedatasphere.dss.standard.common.entity.ref.RequestRef;


public class ExchangisCRUDService extends AbstractRefCRUDService {

    @Override
    protected <K extends CreateRequestRef> RefCreationOperation createRefCreationOperation() {
        return new ExchangisRefCreationOperation(this);
    }

    @Override
    protected <K extends CopyRequestRef> RefCopyOperation<K> createRefCopyOperation() {
        return null;
    }

    @Override
    protected <K extends RequestRef> RefUpdateOperation<K> createRefUpdateOperation() {
        return null;
    }

    @Override
    protected <K extends DeleteRequestRef> RefDeletionOperation<K> createRefDeletionOperation() {
        return null;
    }
}
