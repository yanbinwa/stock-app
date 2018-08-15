package com.yanbinwa.stock.common.response;

import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.constants.Constants;
import com.yanbinwa.stock.utils.DTOUtils;

public enum ReturnInfo
{
    OPERATION_SUCCESS("操作成功", 0),
    ACCOUNT_IS_EXIST("未上传语料或者词库", 1),
    APPLY_STOCK_FAIL("申请交易失败", 2),

    SYSTEM_ERROR("上传语料为空",50);

    private String msg;
    private int code;

    ReturnInfo(String msg, int code)
    {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public int getCode()
    {
        return this.code;
    }

    public static String getReturnStr(Object ret, ReturnInfo returnInfo)
    {
        JsonObject respObj = new JsonObject();
        if (ret != null)
        {
            JsonObject retObj = new JsonObject();
            DTOUtils.copy(ret, respObj);
            respObj.add(Constants.DATA_KEY, retObj);
        }
        respObj.addProperty(Constants.CODE_KEY, returnInfo.getCode());
        respObj.addProperty(Constants.MESSAGE_KEY, returnInfo.getMsg());
        return respObj.toString();
    }

    @Override
    public String toString()
    {
        return this.msg + ": " + code;
    }

}
