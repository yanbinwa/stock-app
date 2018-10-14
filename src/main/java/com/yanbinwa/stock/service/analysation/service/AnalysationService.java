package com.yanbinwa.stock.service.analysation.service;

import com.yanbinwa.stock.service.analysation.request.ChangeRateTrendRequest;
import com.yanbinwa.stock.service.analysation.request.DabanRequest;

public interface AnalysationService {
    void dabanAnalysation(DabanRequest dabanRequest);
    void changeRateTrendAnalysation(ChangeRateTrendRequest changeRateTrendRequest);
}
