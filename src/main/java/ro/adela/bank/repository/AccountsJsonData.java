package ro.adela.bank.repository;

import ro.adela.bank.dto.AmountHistoryDto;
import ro.adela.bank.dto.InterestRateDto;
import ro.adela.bank.dto.SavingsAccountDto;

import java.util.List;

public class AccountsJsonData {

    private List<SavingsAccountDto> accounts;
    private List<AmountHistoryDto> amounts;
    private List<InterestRateDto> interests;

    public List<SavingsAccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<SavingsAccountDto> accounts) {
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
