package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class TotalInterestByDayDto {
    @JsonProperty("starDate")
    private LocalDate starDate;
    @JsonProperty("endDate")
    private LocalDate endDate;
    @JsonProperty("totalInterest")
    private double totalInterest;
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
