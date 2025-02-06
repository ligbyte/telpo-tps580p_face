package com.stkj.cashier.cbgfacepass.net;


import com.stkj.cashier.cbgfacepass.model.BaseNetResponse;
import com.stkj.cashier.cbgfacepass.net.callback.RetrofitConvertJsonListener;

/**
 * 系统json解析监听
 */
public class AppRetrofitJsonConvertListener implements RetrofitConvertJsonListener {
    @Override
    public void onConvertJson(Object o) {
//        if (o instanceof BaseNetResponse) {
//            BaseNetResponse baseNetResponse = (BaseNetResponse) o;
//            //判断token过期
//            if (baseNetResponse.isTokenInvalid() && !LoginHelper.INSTANCE.isHandleLoginValid()) {
//                setNeedHandleLoginValid();
//            }
//        } else if (o instanceof BaseResponse) {
//            BaseResponse baseResponse = (BaseResponse) o;
//            //判断token过期
//            if (baseResponse.isTokenInvalid() && !LoginHelper.INSTANCE.isHandleLoginValid()) {
//                setNeedHandleLoginValid();
//            }
//        }
    }


}
