package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class OutputSummaryAmountDto {

    @JsonProperty("in")
    private double in;
    @JsonProperty("out")
    private double out;
    @JsonProperty("startDate")
    private final LocalDate startDate;
    @JsonProperty("endDate")
    private final LocalDate endDate;
    @JsonProperty("accountNumber")
    private final Integer accountNumber;

    public OutputSummaryAmountDto(LocalDate startDate, LocalDate endDate, Integer accountNumber) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountNumber = accountNumber;
    }

    public Double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public Double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }
}
