package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import ro.adela.bank.enums.OperationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "amount_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@XmlRootElement(name="amount")
@XmlAccessorType(XmlAccessType.FIELD)
public class AmountHistoryDto {
    @Id
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;
    @Column(name = "operationType")
    @XmlElement(name = "operationType")
    @JsonProperty("operationType")
    private OperationType operationType;
    @Column(name = "date")
    @XmlElement(name = "date")
    @JsonProperty("date")
    private LocalDate date;
    @Column(name = "amount")
    @XmlElement(name = "amount")
    @JsonProperty("amount")
    private double amount;
    @Column(name = "currentBalance")
    @XmlElement(name = "currentBalance")
    @JsonProperty("currentBalance")
    private double currentBalance;
}
