package com.cardinal.demo.service;

import com.cardinal.demo.model.RentalAgreement;
import com.cardinal.demo.model.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CheckoutServiceTest {

    private CheckoutService checkoutService;

    @BeforeEach
    public void setUp() {
        Map<String, Tool> tools = new HashMap<>();

        Tool ladder = Tool.builder()
                .code("LADW")
                .type("Ladder")
                .brand("Werner")
                .dailyCharge(1.99)
                .weekdayCharge(true)
                .weekendCharge(true)
                .holidayCharge(false)
                .build();

        Tool chainsaw = Tool.builder()
                .code("CHNS")
                .type("Chainsaw")
                .brand("Stihl")
                .dailyCharge(1.49)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(true)
                .build();

        Tool jackhammerDeWalt = Tool.builder()
                .code("JAKD")
                .type("Jackhammer")
                .brand("DeWalt")
                .dailyCharge(2.99)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .build();

        Tool jackhammerRidgid = Tool.builder()
                .code("JAKR")
                .type("Jackhammer")
                .brand("Ridgid")
                .dailyCharge(2.99)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .build();

        tools.put(ladder.getCode(), ladder);
        tools.put(chainsaw.getCode(), chainsaw);
        tools.put(jackhammerDeWalt.getCode(), jackhammerDeWalt);
        tools.put(jackhammerRidgid.getCode(), jackhammerRidgid);

        checkoutService = new CheckoutService(tools);
    }
    @Test
    public void testInvalidDiscount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
        });

        String expectedMessage = "Discount percent must be in the range 0-100.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testValidCheckout1() {
        RentalAgreement agreement = checkoutService.checkout("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(1, agreement.getChargeDays()); // Expected to be 1 chargeable day (7/5)
        assertEquals(1.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.20, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.79, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testValidCheckout2() {
        RentalAgreement agreement = checkoutService.checkout("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays()); // Expected to be 3 chargeable days (7/3, 7/6, and 7/7)
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testValidCheckout3() {
        RentalAgreement agreement = checkoutService.checkout("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 9), agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays()); // Expected to be 3 chargeable days (9/3, 9/4, and 9/8)
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testValidCheckout4() {
        RentalAgreement agreement = checkoutService.checkout("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 11), agreement.getDueDate());
        assertEquals(5, agreement.getChargeDays()); // Expected to be 5 chargeable days (7/6, 7/7, 7/8, 7/9, 7/10)
        assertEquals(14.95, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(14.95, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testValidCheckout5() {
        RentalAgreement agreement = checkoutService.checkout("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 6), agreement.getDueDate());
        assertEquals(1, agreement.getChargeDays()); // Expected to be 1 chargeable day (7/6)
        assertEquals(2.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.50, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.49, agreement.getFinalCharge(), 0.01);
    }
}
