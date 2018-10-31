package com.yanbinwa.stock.service.analysation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DabanIncomeRequest extends AnalysationBaseRequest{
    private double incomeRate;
    private int tag;
}
