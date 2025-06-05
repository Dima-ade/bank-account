package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.List;

//@Entity
//@Table(name = "bank_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BankDataDto {

    //@Id
    //@JsonIgnore
    //@XmlTransient
    private Integer id;
//    @OneToMany(cascade = CascadeType.ALL)
//    //define the column in the bank account table (the child) which will be
//    // foreign key to the parent table entity (bank data table)
//    @JoinColumn(name = "bank_account_id", nullable = false, referencedColumnName = "id")
    @XmlElement(name = "account")
    @JsonProperty("accounts")
    private List<BankAccountDto> accounts;
//    @OneToMany(cascade = CascadeType.ALL)
//    //define the column in the amount history table (the child) which will be
//    // foreign key to the parent table entity (bank data table)
//    @JoinColumn(name = "amount_history_id", nullable = false, referencedColumnName = "id")
    @XmlElement(name = "amount")
    @JsonProperty("amounts")
    private List<AmountHistoryDto> amounts;
//    @OneToMany(cascade = CascadeType.ALL)
//    //define the column in the interest rate table (the child) which will be
//    // foreign key to the parent table entity (bank data table)
//    @JoinColumn(name = "interest_rate_id", nullable = false, referencedColumnName = "id")
    @XmlElement(name = "interest")
    @JsonProperty("interests")
    private List<InterestRateDto> interests;
}
