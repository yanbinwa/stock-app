package com.yanbinwa.stock.service.collection.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchStockTrendByDateRequest {
    private String startTime;
    private String endTime;
}
