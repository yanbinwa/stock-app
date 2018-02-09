package com.yanbinwa.stock.service.collection.element.IndustryToStockCollection;

import java.util.List;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryToStockCollectionElement
{
    @SerializedName("data")
    @Expose
    private List<IndustryToStock> data;
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
}
