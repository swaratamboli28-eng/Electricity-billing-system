package com.example.ElectricityFinal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {
    private Long id;
    private Long consumerId;
    private String billingMonth;
    private int unitsConsumed;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid = BigDecimal.ZERO;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String status = "UNPAID";

    public Bill() {}

    public Long getId() { return id; }
    public Long getConsumerId() { return consumerId; }
    public String getBillingMonth() { return billingMonth; }
    public int getUnitsConsumed() { return unitsConsumed; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public LocalDate getBillDate() { return billDate; }
    public LocalDate getDueDate() { return dueDate; }
    public String getStatus() { return status; }

    public BigDecimal getOutstandingAmount() {
        return totalAmount.subtract(amountPaid);
    }

    public void setId(Long id) { this.id = id; }
    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }
    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }
    public void setUnitsConsumed(int unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
