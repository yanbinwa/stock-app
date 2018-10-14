package com.yanbinwa.stock.service.analysation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysationBaseRequest {
    protected String startTime;
    protected String endTime;
    protected String stockId;
}
