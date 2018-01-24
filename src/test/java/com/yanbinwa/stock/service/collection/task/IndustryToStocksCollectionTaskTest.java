package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.yanbinwa.stock.common.utils.UnicodeUtils;
import com.yanbinwa.stock.service.collection.element.Industry;

public class IndustryToStocksCollectionTaskTest
{

    public static final String industryName = "研究和试验发展";
    public static final String industryInfo = "#exchange\u003dCN\u0026plate\u003d1_2_64\u0026firstName\u003d1\u0026secondName\u003d1_2\u0026level2code\u003dM73";
    
    @Test
    public void test() throws MalformedURLException, IOException
    {
        Industry industry = new Industry(industryName, UnicodeUtils.unicodeDecode(industryInfo));
        IndustryToStocksCollectionTask task = new IndustryToStocksCollectionTask(industry);
        task.execute();
    }

}
