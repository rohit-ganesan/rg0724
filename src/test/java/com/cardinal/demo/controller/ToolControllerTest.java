package com.cardinal.demo.controller;

import com.cardinal.demo.model.Tool;
import com.cardinal.demo.model.ToolRequest;
import com.cardinal.demo.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

        // Add some tools for testing
        Tool ladder = Tool.builder()
                .code("LADW")
                .type("Ladder")
                .brand("Werner")
                .dailyCharge(1.99)
                .weekdayCharge(true)
                .weekendCharge(true)
                .holidayCharge(false)
                .build();

        tools.put(ladder.getCode(), ladder);
        checkoutService.addTool(ladder);
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

    @Test
    public void testRemoveToolNotFound() {
        ResponseEntity<?> response = toolController.removeTool("NON_EXISTENT_CODE");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Tool with code NON_EXISTENT_CODE not found.", response.getBody());
    }

    @Test
    public void testRemoveToolSuccessfully() {
        ResponseEntity<?> response = toolController.removeTool("LADW");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tool removed successfully.", response.getBody());

        // Verify tool is removed
        assertFalse(tools.containsKey("LADW"));
    }
}
