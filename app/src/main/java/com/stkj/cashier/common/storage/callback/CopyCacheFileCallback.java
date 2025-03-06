package com.stkj.cashier.common.storage.callback;


import com.stkj.cashier.common.storage.model.CacheFileInfo;

import java.util.List;

public interface CopyCacheFileCallback {
    void onSuccess(List<CacheFileInfo> cacheFileInfoList);

    void onError(String errorMsg);
}
