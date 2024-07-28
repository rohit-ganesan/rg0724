package com.cardinal.demo.controller;

import com.cardinal.demo.model.Tool;
import com.cardinal.demo.model.ToolRequest;
import com.cardinal.demo.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final Map<String, Tool> tools;
    private final CheckoutService checkoutService;
    private final Set<String> supportedToolTypes = Set.of("Chainsaw", "Ladder", "Jackhammer");

    @Autowired
    public ToolController(Map<String, Tool> tools, CheckoutService checkoutService) {
        this.tools = tools;
        this.checkoutService = checkoutService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTool(@RequestBody ToolRequest toolRequest) {
        if (!supportedToolTypes.contains(toolRequest.getType())) {
            return ResponseEntity.badRequest().body("Unsupported tool type. You can only add the following tool types: " + String.join(", ", supportedToolTypes));
        }

        Tool tool = Tool.builder()
                .code(toolRequest.getCode())
                .type(toolRequest.getType())
                .brand(toolRequest.getBrand())
                .dailyCharge(toolRequest.getDailyCharge())
                .weekdayCharge(toolRequest.isWeekdayCharge())
                .weekendCharge(toolRequest.isWeekendCharge())
                .holidayCharge(toolRequest.isHolidayCharge())
                .build();

        tools.put(tool.getCode(), tool);
        checkoutService.addTool(tool);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tool added successfully.");
    }
}

