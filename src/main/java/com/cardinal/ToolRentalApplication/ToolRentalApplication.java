package com.cardinal.ToolRentalApplication;

import com.cardinal.ToolRentalApplication.model.RentalAgreement;
import com.cardinal.ToolRentalApplication.service.CheckoutService;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.time.LocalDate;

@SpringBootApplication
public class ToolRentalApplication {
	public static void main(String[] args) {
		CheckoutService checkoutService = new CheckoutService();

		try {
			RentalAgreement agreement = checkoutService.checkout("JAKR", 5, 20,
																	LocalDate.of(2024, 7, 2));
			agreement.printAgreement();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}

