package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ro.adela.bank.enums.OperationType;

import java.time.LocalDate;

@XmlRootElement(name="amount")
@XmlAccessorType(XmlAccessType.FIELD)
public class AmountHistoryDto {
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;
    @XmlElement(name = "operationType")
    @JsonProperty("operationType")
    private OperationType operationType;
    @XmlElement(name = "date")
    @JsonProperty("date")
    private LocalDate date;
    @XmlElement(name = "amount")
    @JsonProperty("amount")
    private double amount;
    @XmlElement(name = "currentBalance")
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
