package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import enums.OperationType;
import jakarta.xml.bind.annotation.*;
import lombok.*;
import enums.OperationType;
import utils.EnumUppercaseConverter;
import utils.EnumUppercaseConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "amount_history")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@XmlRootElement(name="amount")
@XmlAccessorType(XmlAccessType.FIELD)
public class AmountHistoryDto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    @XmlTransient
    private Integer id;

    @Column(name = "accountNumber")
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;

    @Column(name = "operationType")
    @Enumerated(EnumType.STRING)
    @Convert(converter = EnumUppercaseConverter.class)
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
