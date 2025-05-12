package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ro.adela.bank.interfaces.AmountAccount;

import java.time.LocalDate;

public class BankAccountDto implements AmountAccount {

    // Attribute for account number
    @JsonProperty("accountNumber")
    private Integer accountNumber;

    // Attribute for account holder's name
    @JsonProperty("accountHolderName")
    private String accountHolderName;

    // Attribute for account balance
    @JsonProperty("balance")
    private double balance;

    @JsonProperty("birtDate")
    private LocalDate birtDate;

    @JsonProperty("startDate")
    private LocalDate startDate;

    //Default constructor
    public BankAccountDto() {
    }

    // Constructor to initialize BankAccount object
    public BankAccountDto(Integer accountNumber, String accountHolderName, LocalDate birthDate, LocalDate startDate) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.birtDate = birthDate;
        this.startDate = startDate;
        this.balance = 0;
    }

    // Method to check the account balance
    @Override
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Getter method for account number
    public Integer getAccountNumber() {
        return accountNumber;
    }

    // Getter method for account holder's name
    @Override
    public String getAccountHolderName() {
        return accountHolderName;
    }

    public LocalDate getBirtDate() {
        return birtDate;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }
}
