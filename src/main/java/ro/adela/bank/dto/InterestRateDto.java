package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

@XmlRootElement(name="interest")
@XmlAccessorType(XmlAccessType.FIELD)
public class InterestRateDto {

    // Attribute for interest rate
    @XmlElement(name = "interestRate")
    @JsonProperty("interestRate")
    private double interestRate;
    @XmlElement(name = "activationDate")
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
