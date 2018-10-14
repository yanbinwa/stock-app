package com.yanbinwa.stock.service.analysation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DabanRequest extends AnalysationBaseRequest {
    private int dayNum;
    private double group_limit;
    private String dabanTime;
}
