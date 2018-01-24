package com.yanbinwa.stock.common.regular.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegularTaskWarp
{
    public RegularTaskWarp()    
    {
        
    }
    
    @SerializedName("taskName")
    @Expose
    protected String taskName;
    
    @SerializedName("taskClass")
    @Expose
    protected String taskClass;
    
    @SerializedName("uploadStr")
    @Expose
    protected String uploadStr;

}
