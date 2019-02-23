package com.yanbinwa.stock.service.analysation.service;

import com.emotibot.middleware.utils.StringUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.service.analysation.request.*;
import com.yanbinwa.stock.service.analysation.strategy.*;
import com.yanbinwa.stock.service.analysation.task.*;
import com.yanbinwa.stock.utils.DrawStockTrendUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
public class AnalysationServiceImpl implements AnalysationService {

    public static final int WINDOW_GAP = 20;
    public static final String DATE_FORMAT = "yyyyMMdd";

    @Override
    public void dabanAnalysation(DabanRequest dabanRequest) {
        DabanStrategy strategy = new DabanStrategy(dabanRequest.getDayNum(), dabanRequest.getGroup_limit(), dabanRequest.getDabanTime(), WINDOW_GAP);
        clearDir(strategy.getClass().getSimpleName());
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
    public void dabanIncomeAnalysation(DabanIncomeRequest dabanIncomeRequest) {
        DabanIncomeStrategy strategy = new DabanIncomeStrategy(dabanIncomeRequest.getIncomeRate(), dabanIncomeRequest.getTag());
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(dabanIncomeRequest.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(dabanIncomeRequest.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task = new FetchAllIdeaStockTrendTask(FetchAllIdeaStockTrendTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                startDate.getTime(), endDate.getTime(), strategy, null, null);
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
        clearDir(strategy.getClass().getSimpleName());
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

    @Override
    public void lianbanHistoryAnalysation(LianbanHistoryRequest lianbanHistoryRequest) {
        LianBanHistoryStrategy strategy = new LianBanHistoryStrategy(
                lianbanHistoryRequest.getDayNum(),
                lianbanHistoryRequest.getGroup_limit(),
                lianbanHistoryRequest.getWindow_gap()
        );
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(lianbanHistoryRequest.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(lianbanHistoryRequest.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task;
        if (StringUtils.isEmpty(lianbanHistoryRequest.getStockId())) {
            task = new FetchAllIdeaStockTrendTask(FetchAllIdeaStockTrendTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                    startDate.getTime(), endDate.getTime(), strategy, null, null);
        } else {
            task = new FetchAllIdeaStockTrendTask(
                    FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime() + "-" + lianbanHistoryRequest.getStockId(),
                    startDate.getTime(), endDate.getTime(), strategy, lianbanHistoryRequest.getStockId(), null);
        }
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void dabanIncomeByIdAnalysation(DabanIncomeByIdRequest dabanIncomeByIdRequest) {
        DabanIncomeByIdStrategy strategy = new DabanIncomeByIdStrategy(
                dabanIncomeByIdRequest.getIncomeRate(),
                dabanIncomeByIdRequest.getTag()
        );
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(dabanIncomeByIdRequest.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(dabanIncomeByIdRequest.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task;
        if (StringUtils.isEmpty(dabanIncomeByIdRequest.getStockId())) {
            task = new FetchIdeaStockTrendXlsxRootTask(FetchIdeaStockTrendXlsxRootTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                    startDate.getTime(), endDate.getTime(), strategy, null);
        } else {
            task = new FetchIdeaStockTrendXlsxRootTask(
                    FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime() + "-" + dabanIncomeByIdRequest.getStockId(),
                    startDate.getTime(), endDate.getTime(), strategy, dabanIncomeByIdRequest.getStockId());
        }
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void lowPriceStockAnalysation(LowPriceStockRequest lowPriceStockRequest) {
        LowPriceStockStratey strategy = new LowPriceStockStratey(
                lowPriceStockRequest.getPriceRate(),
                lowPriceStockRequest.getWindow_gap(),
                lowPriceStockRequest.getTargetTime()
        );
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(lowPriceStockRequest.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(lowPriceStockRequest.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task;
        if (StringUtils.isEmpty(lowPriceStockRequest.getStockId())) {
            task = new FetchIdeaStockTrendRootTask(FetchIdeaStockTrendRootTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                    startDate.getTime(), endDate.getTime(), strategy);
        } else {
            task = new FetchIdeaStockTrendByIdTask(
                    FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime() + "-" + lowPriceStockRequest.getStockId(),
                    lowPriceStockRequest.getStockId(), startDate.getTime(), endDate.getTime(), strategy);
        }
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void dabanTopNAnalysation(DabanTopNRequest request) {
        DabanTopNStrategy strategy = new DabanTopNStrategy(request.getTopN(), request.getDayNum(), request.getGroup_limit(), request.getWindow_gap());
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(request.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(request.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task = new FetchAllIdeaStockTrendTask(FetchAllIdeaStockTrendTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                startDate.getTime(), endDate.getTime(), strategy, null, null);
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void dabanTopNByIndustryAnalysation(DabanTopNRequest request) {
        DabanTopNStrategy strategy = new DabanTopNStrategy(request.getTopN(), request.getDayNum(), request.getGroup_limit(), request.getWindow_gap());
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(request.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(request.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task = new FetchIdeaStockTrendByIndustryTask(FetchIdeaStockTrendByIndustryTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                startDate.getTime(), endDate.getTime(), strategy, null, null);
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void dabanNumAnalysation(DabanRequest request) {
        DabanNumStrategy strategy = new DabanNumStrategy(request.getDayNum(), request.getGroup_limit(), request.getDabanTime(), WINDOW_GAP);
        clearDir(strategy.getClass().getSimpleName());
        Date startDate = TimeUtils.getDateFromStr(request.getStartTime(), DATE_FORMAT);
        Date endDate = TimeUtils.getDateFromStr(request.getEndTime(), DATE_FORMAT);
        AbstractRegularTask task;
        if (StringUtils.isEmpty(request.getStockId())) {
            task = new FetchIdeaStockTrendRootTask(FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime(),
                    startDate.getTime(), endDate.getTime(), strategy);
        } else {
            task = new FetchIdeaStockTrendByIdTask(
                    FetchIdeaStockTrendByIdTask.class.getSimpleName() + startDate.getTime() + "-" + endDate.getTime() + "-" + request.getStockId(),
                    request.getStockId(), startDate.getTime(), endDate.getTime(), strategy);

        }
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    private void clearDir(String dirPrefix) {
        File dir = new File(DrawStockTrendUtils.DIR + "/" + dirPrefix);
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            dir.delete();
            return;
        } else {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }
        dir.delete();
        return;
    }
}
