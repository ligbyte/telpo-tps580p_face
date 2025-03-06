package com.stkj.cashier.common.storage.callback;


import com.stkj.cashier.common.storage.model.PublicDirFileInfo;

import java.util.List;

public interface GetPublicDirFileCallback {
    void onSuccess(List<PublicDirFileInfo> publicDirFileInfoList);

    void onError(String message);
}
