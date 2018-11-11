package com.yanbinwa.stock.service.analysation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DabanTopNRequest extends AnalysationBaseRequest {
    private int topN;
    private int dayNum;
    private double group_limit;
    private int window_gap;
}
