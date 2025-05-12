package ro.adela.bank.readobject;

import java.time.LocalDate;

public class AddInterestRateReadObject {

    // Attribute for interest rate
    private double interestRate;

    private LocalDate activationDate;

    //Default constructor
    public AddInterestRateReadObject() {
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
