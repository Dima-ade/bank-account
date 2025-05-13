package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ro.adela.bank.interfaces.AmountAccount;

import java.time.LocalDate;

@XmlRootElement(name="account")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccountDto implements AmountAccount {

    // Attribute for account number
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;
    // Attribute for account holder's name
    @XmlElement(name = "accountHolderName")
    @JsonProperty("accountHolderName")
    private String accountHolderName;
    // Attribute for account balance
    @XmlElement(name = "balance")
    @JsonProperty("balance")
    private double balance;
    @XmlElement(name = "birtDate")
    @JsonProperty("birtDate")
    private LocalDate birtDate;
    @XmlElement(name = "startDate")
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
