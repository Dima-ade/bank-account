package ro.adela.bank;

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
    @XmlElement(name = "account")
    @JsonProperty("accounts")
    private List<BankAccountDto> accounts;

    @XmlElement(name = "amount")
    @JsonProperty("amounts")
    private List<AmountHistoryDto> amounts;

    @XmlElement(name = "interest")
    @JsonProperty("interests")
    private List<InterestRateDto> interests;
}
