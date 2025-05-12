package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class InterestRateDto {

    // Attribute for interest rate
    @JsonProperty("interestRate")
    private double interestRate;
    @JsonProperty("activationDate")
    private LocalDate activationDate;

    //Default constructor
    public InterestRateDto() {
    }

    public InterestRateDto(double interestRate, LocalDate activationDate) {
        this.interestRate = interestRate;
        this.activationDate = activationDate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }
}
