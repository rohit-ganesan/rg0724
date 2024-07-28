package com.cardinal.demo.controller;

import com.cardinal.demo.model.RentalAgreement;
import com.cardinal.demo.model.RentalAgreementResponse;
import com.cardinal.demo.model.RentalAgreementRequest;
import com.cardinal.demo.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody RentalAgreementRequest rentalRequest) {
        try {
            RentalAgreement rentalAgreement = checkoutService.checkout(
                    rentalRequest.getToolCode(),
                    rentalRequest.getRentalDays(),
                    rentalRequest.getDiscountPercent(),
                    rentalRequest.getCheckoutDate()
            );

            RentalAgreementResponse response = new RentalAgreementResponse(
                    rentalAgreement.getToolCode(),
                    rentalAgreement.getToolType(),
                    rentalAgreement.getToolBrand(),
                    rentalAgreement.getRentalDays(),
                    rentalAgreement.getCheckoutDate(),
                    rentalAgreement.getDueDate(),
                    rentalAgreement.getDailyRentalCharge(),
                    rentalAgreement.getChargeDays(),
                    rentalAgreement.getPreDiscountCharge(),
                    rentalAgreement.getDiscountPercent(),
                    rentalAgreement.getDiscountAmount(),
                    rentalAgreement.getFinalCharge()
            );
            rentalAgreement.printAgreement();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
