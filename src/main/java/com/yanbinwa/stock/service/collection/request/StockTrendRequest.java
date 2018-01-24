package com.yanbinwa.stock.service.collection.request;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockTrendRequest
{
    @SerializedName("stockIdList")
    @Expose
    private List<String> stockIdList;
    
    @SerializedName("startTimestamp")
    @Expose
    private long startTimestamp;
    
    @SerializedName("endTimestamp")
    @Expose
    private long endTimestamp;
}
