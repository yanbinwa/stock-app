package com.yanbinwa.stock.service.collection.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchHistoryStockTrendRequest {
    private String startTime;
    private String endTime;
}
