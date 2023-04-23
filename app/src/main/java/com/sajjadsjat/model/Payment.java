package com.sajjadsjat.model;

import com.sajjadsjat.enums.PayMethod;

import java.time.LocalDateTime;

public class Payment {
    public LocalDateTime creationDateTime;
    public double amount;
    public double discount;
    public PayMethod payMethod;
}
