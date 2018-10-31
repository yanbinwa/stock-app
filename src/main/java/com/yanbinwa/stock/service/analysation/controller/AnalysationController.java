package com.yanbinwa.stock.service.analysation.controller;

import com.yanbinwa.stock.service.analysation.request.*;
import com.yanbinwa.stock.service.analysation.service.AnalysationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysation")
public class AnalysationController {

    @Autowired
    AnalysationService analysationService;

    @PostMapping(value = "/dabanAnalysation")
    public void dabanAnalysation(@RequestBody DabanRequest request) {
        analysationService.dabanAnalysation(request);
    }

    @PostMapping(value = "/changeRateTrendAnalysation")
    public void changeRateTrendAnalysation(@RequestBody ChangeRateTrendRequest request) {
        analysationService.changeRateTrendAnalysation(request);
    }

    @PostMapping(value = "/dabanIncomeAnalysation")
    public void dabanIncomeAnalysation(@RequestBody DabanIncomeRequest request) {
        analysationService.dabanIncomeAnalysation(request);
    }

    @PostMapping(value = "/lianbanHistoryAnalysation")
    public void lianbanHistoryAnalysation(@RequestBody LianbanHistoryRequest request) {
        analysationService.lianbanHistoryAnalysation(request);
    }

    @PostMapping(value = "/dabanIncomeByIdAnalysation")
    public void dabanIncomeByIdAnalysation(@RequestBody DabanIncomeByIdRequest request) {
        analysationService.dabanIncomeByIdAnalysation(request);
    }
}
