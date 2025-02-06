package com.stkj.cashier.cbgfacepass.service;


import com.stkj.cashier.cbgfacepass.model.BaseNetResponse;
import com.stkj.cashier.cbgfacepass.model.FacePassPeopleListInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SettingService {

    /**
     * 设备录入人员回调(同步)
     */
    @GET("home/v3/index")
    Call<BaseNetResponse<String>> syncFacePassCallback(@QueryMap Map<String, String> requestParams);



    /**
     * 设备录入人员信息接口
     */
    @GET("home/v2/index")
    Observable<BaseNetResponse<FacePassPeopleListInfo>> getAllFacePass(@QueryMap Map<String, String> requestParams);

}
