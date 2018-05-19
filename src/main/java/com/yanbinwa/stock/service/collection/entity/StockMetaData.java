package com.yanbinwa.stock.service.collection.entity;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockMetaData
{
    @SerializedName("stockId")
    @Expose
    private String stockId;
    
    @SerializedName("name")
    @Expose
    private String name;
    
    @SerializedName("volume")
    @Expose
    private double volume;
    
    public StockMetaData()
    {
        
    }
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
}
