package com.yanbinwa.stock.service.analysation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowPriceStockRequest extends AnalysationBaseRequest {
    private double priceRate;
    private int window_gap;
    private String targetTime;
}
