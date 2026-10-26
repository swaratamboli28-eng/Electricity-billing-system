package com.example.ElectricityFinal.service;

import com.example.ElectricityFinal.model.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElectricityService {

    private final List<Consumer> consumers = new ArrayList<>();
    private final List<Bill> bills = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();

    private long nextConsumerId = 1;
    private long nextBillId = 1;
    private long nextPaymentId = 1;

    // Register a consumer
    public Consumer addConsumer(Consumer consumer) {
        if (consumer.getName() == null ||
                consumer.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Name is required"
            );
        }

        consumer.setId(nextConsumerId++);
        consumers.add(consumer);
        return consumer;
    }

    public List<Consumer> getConsumers() {
        return new ArrayList<>(consumers);
    }

    private Consumer findConsumer(Long id) {
        return consumers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consumer not found"
                ));
    }

    private Bill findBill(Long id) {
        return bills.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Bill not found"
                ));
    }

    // Slab-based bill calculation
    public BigDecimal calculateCharges(int units) {
        if (units < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Units cannot be negative"
            );
        }

        BigDecimal charge;

        if (units <= 100) {
            charge = BigDecimal.valueOf(units * 3L);
        } else if (units <= 200) {
            charge = BigDecimal.valueOf(
                    300L + (units - 100L) * 5L
            );
        } else {
            charge = BigDecimal.valueOf(
                    800L + (units - 200L) * 7L
            );
        }

        // Sample fixed charge: ₹50
        return charge.add(BigDecimal.valueOf(50))
                .setScale(2);
    }

    // Generate one bill per consumer per month
    public Bill generateBill(Long consumerId,
                             String billingMonth,
                             int units) {
        findConsumer(consumerId);

        if (units < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Units cannot be negative"
            );
        }

        YearMonth month;
        try {
            month = YearMonth.parse(billingMonth);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Month must be in YYYY-MM format"
            );
        }

        boolean exists = bills.stream().anyMatch(b ->
                b.getConsumerId().equals(consumerId)
                        && b.getBillingMonth().equals(month.toString())
        );

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Bill already exists for this month"
            );
        }

        LocalDate today = LocalDate.now();

        Bill bill = new Bill();
        bill.setId(nextBillId++);
        bill.setConsumerId(consumerId);
        bill.setBillingMonth(month.toString());
        bill.setUnitsConsumed(units);
        bill.setTotalAmount(calculateCharges(units));
        bill.setAmountPaid(BigDecimal.ZERO);
        bill.setBillDate(today);
        bill.setDueDate(today.plusDays(15));
        bill.setStatus("UNPAID");

        bills.add(bill);
        return bill;
    }

    // Record full or partial payment
    public Payment payBill(Long billId, BigDecimal amount) {
        Bill bill = findBill(billId);

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment must be greater than zero"
            );
        }

        if (amount.scale() > 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment can have at most two decimal places"
            );
        }

        if (amount.compareTo(
                bill.getOutstandingAmount()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment exceeds outstanding dues"
            );
        }

        Payment payment = new Payment(
                nextPaymentId++,
                billId,
                amount,
                LocalDate.now()
        );

        payments.add(payment);

        BigDecimal updatedPaid =
                bill.getAmountPaid().add(amount);

        bill.setAmountPaid(updatedPaid);

        if (updatedPaid.compareTo(bill.getTotalAmount()) == 0) {
            bill.setStatus("PAID");
        } else {
            bill.setStatus("PARTIALLY_PAID");
        }

        return payment;
    }

    // View billing history, newest first
    public List<Bill> getBillingHistory(Long consumerId) {
        findConsumer(consumerId);

        return bills.stream()
                .filter(b -> b.getConsumerId().equals(consumerId))
                .sorted((a, b) ->
                        b.getBillingMonth().compareTo(a.getBillingMonth()))
                .toList();
    }

    // View unpaid or partially paid bills
    public List<Bill> getOutstandingDues(Long consumerId) {
        findConsumer(consumerId);

        return bills.stream()
                .filter(b -> b.getConsumerId().equals(consumerId))
                .filter(b -> b.getOutstandingAmount()
                        .compareTo(BigDecimal.ZERO) > 0)
                .toList();
    }

    // View payments for a bill
    public List<Payment> getPayments(Long billId) {
        findBill(billId);

        return payments.stream()
                .filter(p -> p.getBillId().equals(billId))
                .toList();
    }
}
