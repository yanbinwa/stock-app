package com.yanbinwa.stock.service.analysation.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yanbinwa.stock.service.analysation.strategy.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.service.analysation.script.ScriptCollection;
import com.yanbinwa.stock.service.analysation.script.ScriptCompare;
import com.yanbinwa.stock.service.analysation.script.ScriptEle;
import com.yanbinwa.stock.service.analysation.script.ScriptTarget;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class FetchIdeaStockTrendByIdTaskTest
{
    @Test
    public void test()
    {
        Date startDate = TimeUtils.getDateFromStr("20181018", "yyyyMMdd");
        Date endDate = TimeUtils.getDateFromStr("20181101", "yyyyMMdd");
        FetchIdeaStockTrendByIdTask task = new FetchIdeaStockTrendByIdTask("FetchIdeaStockTrendByIdTask", "SZ300022", startDate.getTime(), endDate.getTime(), getStrategy1());
        task.execute();
    }

    public static Strategy getStrategy1()
    {
        return new ContinueIncreaseStrategy(4, 15d, 20);
    }
    
    public static Strategy getStrategy2()
    {
        List<ScriptCollection> scriptCollections = new ArrayList<ScriptCollection>();
        List<ScriptEle> scriptEleList1 = new ArrayList<ScriptEle>();
        ScriptEle scriptEle1 = new ScriptEle(ScriptTarget.PERCENT, ScriptCompare.GTE, 1.3d);
        scriptEleList1.add(scriptEle1);
        ScriptCollection scriptCollection1 = new ScriptCollection(scriptEleList1);
        scriptCollections.add(scriptCollection1);
        
        List<ScriptEle> scriptEleList2 = new ArrayList<ScriptEle>();
        ScriptEle scriptEle2 = new ScriptEle(ScriptTarget.PERCENT, ScriptCompare.GTE, 10d);
        scriptEleList2.add(scriptEle2);
        ScriptCollection scriptCollection2 = new ScriptCollection(scriptEleList2);
        scriptCollections.add(scriptCollection2);
        
        return new ScriptStrategy(scriptCollections);
    }

    public static Strategy getStrategy3() {
        return new ContinueIncreaseStrategy2(4, 10);
    }

    public static Strategy getStrategy4() {
        return new ChangeRateTrendStrategy(4, 5, 7, 3, 10);
    }

    public static Strategy getStrategy5() {
        return new DabanStrategy(2, 9.98, null, 20);
    }
}
