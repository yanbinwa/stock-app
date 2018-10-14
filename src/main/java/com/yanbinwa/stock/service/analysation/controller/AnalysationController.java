package com.yanbinwa.stock.service.analysation.controller;

import com.yanbinwa.stock.service.analysation.request.ChangeRateTrendRequest;
import com.yanbinwa.stock.service.analysation.request.DabanRequest;
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
}
