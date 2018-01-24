package com.yanbinwa.stock.service.collection.element;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Industry
{
    
    @SerializedName("industryName")
    @Expose
    private String industryName;

    @SerializedName("industryInfo")
    @Expose
    private String industryInfo;
    
    public Industry()
    {
        
    }
    
    public Industry(String industryName, String industryInfo)
    {
        this.industryName = industryName;
        this.industryInfo = industryInfo;
    }
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
    
    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Industry))
        {
            return false;
        }
        Industry other = (Industry) obj;
        if (this.toString().equals(other.toString()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
