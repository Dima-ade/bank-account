package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

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

    public List<BankAccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountDto> accounts) {
        this.accounts = accounts;
    }

    public List<AmountHistoryDto> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<AmountHistoryDto> amounts) {
        this.amounts = amounts;
    }

    public List<InterestRateDto> getInterests() {
        return this.interests;
    }

    public void setInterests(List<InterestRateDto> interests) {
        this.interests = interests;
    }
}
