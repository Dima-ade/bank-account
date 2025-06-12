package ro.adela.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import interfaces.AmountAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@XmlRootElement(name="account")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccountDto implements AmountAccount {

    // Attribute for account number
    @Id
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;

    // Attribute for account holder's name
    @Column(name = "accountHolderName")
    @XmlElement(name = "accountHolderName")
    @JsonProperty("accountHolderName")
    private String accountHolderName;

    // Attribute for account balance
    @Column(name = "balance")
    @XmlElement(name = "balance")
    @JsonProperty("balance")
    private double balance;

    @Column(name = "birtDate")
    @XmlElement(name = "birtDate")
    @JsonProperty("birtDate")
    private LocalDate birtDate;

    @Column(name = "startDate")
    @XmlElement(name = "startDate")
    @JsonProperty("startDate")
    private LocalDate startDate;
}
