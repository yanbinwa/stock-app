package com.yanbinwa.stock.service.analysation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRateTrendRequest extends AnalysationBaseRequest{

    private int lowChangeRateDay;
    private double lowChangeRate;

    private int targetChangeRateDay;
    private double targetChangeRate;

}
