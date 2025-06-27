package ro.adela.bank.service;

import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.InterestRateDto;
import ro.adela.bank.repository.AmountHistoryRepository;
import ro.adela.bank.repository.BankAccountRepository;
import ro.adela.bank.repository.InterestRateRepository;

import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DatabaseService extends AbstractDatabaseService {

    private final BankAccountRepository bankAccountRepository;
    private final AmountHistoryRepository amountHistoryRepository;

    private final InterestRateRepository interestRateRepository;

    public DatabaseService(EntityManagerFactory emf) {
        super(emf);

        bankAccountRepository = new BankAccountRepository(emf);
        amountHistoryRepository = new AmountHistoryRepository(emf);
        interestRateRepository = new InterestRateRepository(emf);

    }

    public DatabaseService() {
        bankAccountRepository = new BankAccountRepository(emf);
        amountHistoryRepository = new AmountHistoryRepository(emf);
        interestRateRepository = new InterestRateRepository(emf);
    }

    protected BankAccountDto findAccountByNumber(Integer accountNumber) {
        return bankAccountRepository.findById(accountNumber).get();
    }

    protected void saveAccount(BankAccountDto savingsAccount) {
        bankAccountRepository.save(savingsAccount);
    }

    protected void saveAmountHistory(AmountHistoryDto emp) {
        amountHistoryRepository.save(emp);
    }

    protected void saveInterestRate(InterestRateDto interestRate) {
        interestRateRepository.save(interestRate);
    }

    protected List<InterestRateDto> findAllInterestRate() {
        return interestRateRepository.findAll();
    }

    protected List<AmountHistoryDto> findAllAmounts() {
        return amountHistoryRepository.findAll();
    }

    protected Integer amountTotalCount() {
        return amountHistoryRepository.totalCount();
    }

    protected List<AmountHistoryDto> getAmountsByPage(int pageIndex, int pageSize) {
        List<AmountHistoryDto> amounts = amountHistoryRepository.findByPage(pageIndex + 1, pageSize);

        return amounts;
    }

    protected List<InterestRateDto> getInterestByPageInRepository(Integer pageNumber, Integer pageSize) {
        List<InterestRateDto> interests = interestRateRepository.findByPage(pageNumber, pageSize);

        return interests;
    }

}
