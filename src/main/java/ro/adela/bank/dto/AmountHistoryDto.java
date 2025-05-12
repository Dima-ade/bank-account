package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ro.adela.bank.enums.OperationType;

import java.time.LocalDate;

public class AmountHistoryDto {
    @JsonProperty("accountNumber")
    private Integer accountNumber;
    @JsonProperty("operationType")
    private OperationType operationType;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("currentBalance")
    private double currentBalance;

    public AmountHistoryDto() {
    }

    public AmountHistoryDto(Integer accountNumber, OperationType operationType, LocalDate date, double amount, double currentBalance) {
        this.accountNumber = accountNumber;
        this.operationType = operationType;
        this.date = date;
        this.amount = amount;
        this.currentBalance = currentBalance;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Enum<OperationType> getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
