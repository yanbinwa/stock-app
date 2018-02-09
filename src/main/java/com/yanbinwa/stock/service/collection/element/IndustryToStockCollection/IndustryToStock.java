package com.yanbinwa.stock.service.collection.element.IndustryToStockCollection;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryToStock
{
    @SerializedName("symbol")
    @Expose
    private String symbol;
    
    @SerializedName("amount")
    @Expose
    private double amount;
    
    @SerializedName("chg")
    @Expose
    private double chg;
    
    @SerializedName("hasexist")
    @Expose
    private boolean hasexist;
    
    @SerializedName("high52w")
    @Expose
    private double high52w;
    
    @SerializedName("last_close")
    @Expose
    private double last_close;
    
    @SerializedName("percent")
    @Expose
    private double percent;
    
    @SerializedName("volume")
    @Expose
    private double volume;
    
    @SerializedName("current")
    @Expose
    private double current;
    
    @SerializedName("high")
    @Expose
    private double high;
    
    @SerializedName("low")
    @Expose
    private double low;
    
    @SerializedName("name")
    @Expose
    private String name;
    
    @SerializedName("market_capital")
    @Expose
    private double market_capital;
    
    @SerializedName("low52w")
    @Expose
    private double low52w;
    
    @SerializedName("pe_ttm")
    @Expose
    private double pe_ttm;
    
    @SerializedName("open")
    @Expose
    private double open;
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
}
