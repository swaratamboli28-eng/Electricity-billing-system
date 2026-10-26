package com.example.ElectricityFinal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private Long id;
    private Long billId;
    private BigDecimal amount;
    private LocalDate paymentDate;

    public Payment() {}

    public Payment(Long id, Long billId,
                   BigDecimal amount, LocalDate paymentDate) {
        this.id = id;
        this.billId = billId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public Long getId() { return id; }
    public Long getBillId() { return billId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }

    public void setId(Long id) { this.id = id; }
    public void setBillId(Long billId) { this.billId = billId; }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
