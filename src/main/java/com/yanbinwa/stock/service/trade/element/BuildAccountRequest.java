package com.yanbinwa.stock.service.trade.element;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildAccountRequest {
    private String name;
    private double deposit;
}
