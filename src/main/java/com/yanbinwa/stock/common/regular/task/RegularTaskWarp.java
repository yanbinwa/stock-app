package com.yanbinwa.stock.common.regular.task;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularTaskWarp
{
    
    @SerializedName("taskName")
    @Expose
    protected String taskName;
    
    @SerializedName("taskClass")
    @Expose
    protected String taskClass;
    
    @SerializedName("uploadStr")
    @Expose
    protected String uploadStr;

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof RegularTaskWarp))
        {
            return false;
        }
        RegularTaskWarp other = (RegularTaskWarp) obj;
        return this.toString().equals(other.toString());
    }
    
    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
}
