package com.cardinal.demo.controller;

import com.cardinal.demo.model.Tool;
import com.cardinal.demo.model.ToolRequest;
import com.cardinal.demo.service.CheckoutService;
import com.cardinal.demo.model.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ToolControllerTest {

    private ToolController toolController;
    private CheckoutService checkoutService;
    private Map<String, Tool> tools;

    @BeforeEach
    public void setUp() {
        tools = new HashMap<>();
        checkoutService = new CheckoutService(tools);

        // Manually call the init method to simulate Spring's @PostConstruct behavior
        checkoutService.init("Chainsaw,Ladder,Jackhammer");

        toolController = new ToolController(tools, checkoutService);
    }

    @Test
    public void testAddUnsupportedTool() {
        ToolRequest toolRequest = new ToolRequest();
        toolRequest.setCode("NEW1");
        toolRequest.setType("Drill");
        toolRequest.setBrand("Bosch");
        toolRequest.setDailyCharge(3.99);
        toolRequest.setWeekdayCharge(true);
        toolRequest.setWeekendCharge(false);
        toolRequest.setHolidayCharge(false);

        ResponseEntity<?> response = toolController.addTool(toolRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unsupported tool type. You can only add the following tool types: Chainsaw, Ladder, Jackhammer", response.getBody());
    }

    @Test
    public void testAddSupportedTool() {
        ToolRequest toolRequest = new ToolRequest();
        toolRequest.setCode("LADW");
        toolRequest.setType("Ladder");
        toolRequest.setBrand("Werner");
        toolRequest.setDailyCharge(1.99);
        toolRequest.setWeekdayCharge(true);
        toolRequest.setWeekendCharge(true);
        toolRequest.setHolidayCharge(false);

        ResponseEntity<?> response = toolController.addTool(toolRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tool added successfully.", response.getBody());
    }
}
