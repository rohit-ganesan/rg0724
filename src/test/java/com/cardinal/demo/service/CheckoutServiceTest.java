package com.cardinal.demo.service;

import com.cardinal.demo.model.RentalAgreement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CheckoutServiceTest {

    private CheckoutService checkoutService = new CheckoutService();

    @Test
    public void testValidCheckout1() {
        RentalAgreement agreement = checkoutService.checkout("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(1, agreement.getChargeDays()); // Expected to be 1 chargeable day
        assertEquals(1.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.20, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.79, agreement.getFinalCharge(), 0.01);
    }

    // Add additional tests for the other scenarios
}
