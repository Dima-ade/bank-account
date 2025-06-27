package ro.adela.bank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.InterestRateDto;
import ro.adela.bank.jpa.repository.JpaAmountHistoryRepository;
import ro.adela.bank.jpa.repository.JpaBankAccountRepository;
import ro.adela.bank.jpa.repository.JpaInterestRateRepository;

import java.util.List;

public class JpaDatabaseService extends AbstractDatabaseService {

    private final PersistenceManager persistenceManager;

    public JpaDatabaseService(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;

    }

    @Override
    protected void saveAccount(BankAccountDto savingsAccount) {
        JpaBankAccountRepository bankAccountRepository = this.persistenceManager.getJpaBankAccountRepository();
        bankAccountRepository.save(savingsAccount);
    }

    @Override
    protected BankAccountDto findAccountByNumber(Integer accountNumber) {
        JpaBankAccountRepository bankAccountRepository = this.persistenceManager.getJpaBankAccountRepository();
        return bankAccountRepository.findById(accountNumber).get();
    }

    @Override
    protected void saveAmountHistory(AmountHistoryDto amountHistoryDto) {
        JpaAmountHistoryRepository amountHistoryRepository = this.persistenceManager.getJpaAmountHistoryRepository();
        amountHistoryRepository.save(amountHistoryDto);
    }

    @Override
    protected void saveInterestRate(InterestRateDto interestRate) {
        JpaInterestRateRepository interestRateRepository = this.persistenceManager.getJpaInterestRateRepository();
        interestRateRepository.save(interestRate);
    }

    @Override
    protected List<InterestRateDto> findAllInterestRate() {
        JpaInterestRateRepository interestRateRepository = this.persistenceManager.getJpaInterestRateRepository();
        return interestRateRepository.findAll();
    }

    @Override
    protected List<AmountHistoryDto> findAllAmounts() {
        JpaAmountHistoryRepository amountHistoryRepository = this.persistenceManager.getJpaAmountHistoryRepository();
        return amountHistoryRepository.findAll();
    }

    @Override
    protected Integer amountTotalCount() {
        JpaAmountHistoryRepository amountHistoryRepository = this.persistenceManager.getJpaAmountHistoryRepository();
        return amountHistoryRepository.totalCount();
    }

    @Override
    protected List<AmountHistoryDto> getAmountsByPage(int pageIndex, int pageSize) {
        JpaAmountHistoryRepository amountHistoryRepository = this.persistenceManager.getJpaAmountHistoryRepository();

        Pageable pagesWithSizeElements = PageRequest.of(pageIndex, pageSize);
        Page<AmountHistoryDto> amountsInPage = amountHistoryRepository.findAll(pagesWithSizeElements);

        return amountsInPage.getContent();
    }

    @Override
    protected List<InterestRateDto> getInterestByPageInRepository(Integer pageNumber, Integer pageSize) {
        JpaInterestRateRepository interestRateRepository = this.persistenceManager.getJpaInterestRateRepository();

        Pageable pagesWithSizeElements = PageRequest.of(pageNumber, pageSize);
        Page<InterestRateDto> interestRatesInPage = interestRateRepository.findAll(pagesWithSizeElements);

        return interestRatesInPage.getContent();
    }
}
