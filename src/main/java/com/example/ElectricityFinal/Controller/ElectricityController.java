package com.example.ElectricityFinal.Controller;

import com.example.ElectricityFinal.model.*;
import com.example.ElectricityFinal.service.ElectricityService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ElectricityController {

    private final ElectricityService service;

    public ElectricityController(ElectricityService service) {
        this.service = service;
    }

    @PostMapping("/consumers")
    public Consumer addConsumer(
            @RequestBody Consumer consumer) {
        return service.addConsumer(consumer);
    }

    @GetMapping("/consumers")
    public List<Consumer> getConsumers() {
        return service.getConsumers();
    }

    @GetMapping("/calculate")
    public BigDecimal calculateCharges(
            @RequestParam int units) {
        return service.calculateCharges(units);
    }

    @PostMapping("/bills")
    public Bill generateBill(
            @RequestParam Long consumerId,
            @RequestParam String billingMonth,
            @RequestParam int units) {
        return service.generateBill(
                consumerId, billingMonth, units
        );
    }

    @PostMapping("/bills/{billId}/payments")
    public Payment payBill(
            @PathVariable Long billId,
            @RequestParam BigDecimal amount) {
        return service.payBill(billId, amount);
    }

    @GetMapping("/consumers/{consumerId}/bills")
    public List<Bill> getBillingHistory(
            @PathVariable Long consumerId) {
        return service.getBillingHistory(consumerId);
    }

    @GetMapping("/consumers/{consumerId}/dues")
    public List<Bill> getOutstandingDues(
            @PathVariable Long consumerId) {
        return service.getOutstandingDues(consumerId);
    }

    @GetMapping("/bills/{billId}/payments")
    public List<Payment> getPayments(
            @PathVariable Long billId) {
        return service.getPayments(billId);
    }
}
