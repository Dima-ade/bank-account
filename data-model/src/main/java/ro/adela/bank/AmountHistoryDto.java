package ro.adela.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import enums.OperationType;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.*;
import enums.OperationType;
import utils.EnumUppercaseConverter;
import utils.EnumUppercaseConverter;
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
    @Column(name = "id")
    @JsonIgnore
    @XmlTransient
    private Integer id;

    @Column(name = "accountnumber")
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;

    @Column(name = "operationtype")
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

    @Column(name = "currentbalance")
    @XmlElement(name = "currentBalance")
    @JsonProperty("currentBalance")
    private double currentBalance;
}
