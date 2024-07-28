package com.cardinal.demo.controller;

import com.cardinal.demo.model.ToolRequest;
import com.cardinal.demo.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final CheckoutService checkoutService;

    @Autowired
    public ToolController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTool(@RequestBody ToolRequest toolRequest) {
        checkoutService.addTool(toolRequest);
        return new ResponseEntity<>("Tool added successfully", HttpStatus.CREATED);
    }
}

