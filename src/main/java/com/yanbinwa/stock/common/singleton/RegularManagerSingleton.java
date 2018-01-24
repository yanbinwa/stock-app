package com.yanbinwa.stock.common.singleton;

import com.yanbinwa.stock.common.regular.manager.RegularManager;
import com.yanbinwa.stock.common.regular.manager.RegularManagerImpl;

public class RegularManagerSingleton
{
    private static RegularManager regularManager = null;
    
    public static RegularManager getInstance()
    {
        if (regularManager == null)
        {
            regularManager = new RegularManagerImpl();
        }
        return regularManager;
    }
}
