package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TotalInterestByDayDto {
    @XmlElement(name = "starDate")
    @JsonProperty("starDate")
    private LocalDate starDate;
    @XmlElement(name = "endDate")
    @JsonProperty("endDate")
    private LocalDate endDate;
    @XmlElement(name = "totalInterest")
    @JsonProperty("totalInterest")
    private double totalInterest;
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;

    public TotalInterestByDayDto() {
    }

    public TotalInterestByDayDto(LocalDate starDate, LocalDate endDate, Integer accountNumber) {
        this.starDate = starDate;
        this.endDate = endDate;
        this.accountNumber = accountNumber;
    }

    public LocalDate getStarDate() {
        return this.starDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public double getTotalInterest() {
        return this.totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Integer getAccountNumber() {
        return this.accountNumber;
    }
}
