package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OutputSummaryAmountDto {

    @Column(name = "in")
    @XmlElement(name = "in")
    @JsonProperty("in")
    private double in;
    @Column(name = "out")
    @XmlElement(name = "out")
    @JsonProperty("out")
    private double out;
    @Column(name = "startDate")
    @XmlElement(name = "startDate")
    @JsonProperty("startDate")
    private final LocalDate startDate;
    @Column(name = "endDate")
    @XmlElement(name = "endDate")
    @JsonProperty("endDate")
    private final LocalDate endDate;
    @Id
    @XmlElement(name = "accountNumber")
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
