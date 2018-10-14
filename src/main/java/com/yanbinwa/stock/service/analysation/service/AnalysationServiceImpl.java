package com.yanbinwa.stock.service.analysation.service;

import com.emotibot.middleware.utils.StringUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.service.analysation.request.ChangeRateTrendRequest;
import com.yanbinwa.stock.service.analysation.request.DabanRequest;
import com.yanbinwa.stock.service.analysation.strategy.ChangeRateTrendStrategy;
import com.yanbinwa.stock.service.analysation.strategy.DabanStrategy;
import com.yanbinwa.stock.service.analysation.task.FetchIdeaStockTrendByIdTask;
import com.yanbinwa.stock.service.analysation.task.FetchIdeaStockTrendRootTask;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnalysationServiceImpl implements AnalysationService {

    public static final int WINDOW_GAP = 20;
    public static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public void dabanAnalysation(DabanRequest dabanRequest) {
        DabanStrategy strategy = new DabanStrategy(dabanRequest.getDayNum(), dabanRequest.getGroup_limit(), dabanRequest.getDabanTime(), WINDOW_GAP);
        Date startDate = TimeUtils.getDateFromStr(dabanRequest.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(dabanRequest.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task;
        if (StringUtils.isEmpty(dabanRequest.getStockId())) {
            task = new FetchIdeaStockTrendRootTask(FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                    startDate.getTime(), endDate.getTime(), strategy);
        } else {
            task = new FetchIdeaStockTrendByIdTask(
                    FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime() + "-" + dabanRequest.getStockId(),
                    dabanRequest.getStockId(), startDate.getTime(), endDate.getTime(), strategy);

        }
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void changeRateTrendAnalysation(ChangeRateTrendRequest changeRateTrendRequest) {
        ChangeRateTrendStrategy strategy = new ChangeRateTrendStrategy(
                changeRateTrendRequest.getLowChangeRateDay(),
                changeRateTrendRequest.getLowChangeRate(),
                changeRateTrendRequest.getTargetChangeRateDay(),
                changeRateTrendRequest.getTargetChangeRate(),
                WINDOW_GAP);
        Date startDate = TimeUtils.getDateFromStr(changeRateTrendRequest.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(changeRateTrendRequest.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task;
        if (StringUtils.isEmpty(changeRateTrendRequest.getStockId())) {
            task = new FetchIdeaStockTrendRootTask(FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                    startDate.getTime(), endDate.getTime(), strategy);
        } else {
            task = new FetchIdeaStockTrendByIdTask(
                    FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime() + "-" + changeRateTrendRequest.getStockId(),
                    changeRateTrendRequest.getStockId(), startDate.getTime(), endDate.getTime(), strategy);

        }
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }
}
