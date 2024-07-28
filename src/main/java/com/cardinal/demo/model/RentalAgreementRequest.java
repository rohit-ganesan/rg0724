package com.cardinal.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
public class RentalAgreementRequest {
    private String toolCode;
    private int rentalDays;
    private int discountPercent;
    private LocalDate checkoutDate;

}

