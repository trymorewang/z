package com.design.chain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Request {

    private String name;
    private BigDecimal amount;
}
