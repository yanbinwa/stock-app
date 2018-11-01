package com.yanbinwa.stock.service.analysation.service;

import com.yanbinwa.stock.service.analysation.request.*;

public interface AnalysationService {
    void dabanAnalysation(DabanRequest dabanRequest);
    void dabanIncomeAnalysation(DabanIncomeRequest dabanIncomeRequest);
    void changeRateTrendAnalysation(ChangeRateTrendRequest changeRateTrendRequest);
    void lianbanHistoryAnalysation(LianbanHistoryRequest lianbanHistoryRequest);
    void dabanIncomeByIdAnalysation(DabanIncomeByIdRequest dabanIncomeByIdRequest);
    void lowPriceStockAnalysation(LowPriceStockRequest request);
}
